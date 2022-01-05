package com.example.restfulwebservice.user;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/*** Level3 단계의 REST API 구현을 위한 HATEOAS 적용 */
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * 실제 DB 연동하지 않고 UserDaoService에서 더미 데이터를 반환
 */
@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/1 or /users/10 -> String
    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS
        /*** Level3 단계의 REST API 구현을 위한 HATEOAS 적용
         *  - 현재 리소스와 연관된(호출 가능한) 자원 상태 정보를 제공 */
        Resource<User>  resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }
    /*** links 로 HATEOS 정보 반환
     * {
     *     "id": 1,
     *     "name": "Kenneth",
     *     "joinDate": "2022-01-05T14:28:43.576+0000",
     *     "password": "pass1",
     *     "ssn": "701010-1111111",
     *     "posts": null,
     *     "links": [
     *         {
     *             "rel": "all-users",
     *             "href": "http://localhost:8088/users",
     *             "hreflang": null,
     *             "media": null,
     *             "title": null,
     *             "type": null,
     *             "deprecation": null
     *         }
     *     ]
     * }
     */

    /*** @Valid : 객체에 등록된 Validation 사용 */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        /**
         * 사용자 추가 이후 어떤 URI로 확인(사용자 상세정보 API)이 가능한지 정보를 반환
         *  - Http Status Code : 201 OK (Created) 반환
         *  - Response Headers 의 Location에 "http://localhost:8088/users/4" 반환 (추가된 정보 확인 URI)
         * */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())  // 저장된 ID 값 설정
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

    // {id: "1", name: "new name", password: "new password"}
    @PutMapping("/users/{id}")
    public Resource<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User updatedUser = service.update(id, user);

        if (updatedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", user.getId()));
        }

        // HATEOAS
        Resource<User>  resource = new Resource<>(updatedUser);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }
}
