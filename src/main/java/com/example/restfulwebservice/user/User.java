package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password", "ssn"})  /*** 응답에 해당 값 제거*/
@NoArgsConstructor
/*** * RESTful Service 기능 확장 - 프로그래밍으로 제어하는 Filtering 방법 - 개별/전체 사용자 조회 * */
//@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
/***  Spring Data JPA를 이용한 Entity 설정과 초기 데이터 생성 * */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.")
    @ApiModelProperty(notes = "사용자 이름을 입력해 주세요.")
    private String name;
    @Past /*** 가입시점 과거 날짜만 가능 */
    @ApiModelProperty(notes = "사용자의 등록일을 입력해 주세요.")
    private Date joinDate;

//    @JsonIgnore /*** 응답에 해당 값 제거*/
    @ApiModelProperty(notes = "사용자의 패스워드를 입력해 주세요.")
    private String password;

//    @JsonIgnore /*** 응답에 해당 값 제거*/
    @ApiModelProperty(notes = "사용자의 주민번호를 입력해 주세요.")
    private String ssn;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User(int id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
