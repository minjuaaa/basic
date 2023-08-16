package com.plete.basic;


import com.plete.basic.question.Question;
import com.plete.basic.question.QuestionForm;
import com.plete.basic.question.QuestionService;
import com.plete.basic.user.SiteUser;
import com.plete.basic.user.UserCreateForm;
import com.plete.basic.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final QuestionService questionService;
    private final UserService userService;


    // 홈 화면에서 question 리스트 출력, 페이징
    @GetMapping("/")
    // /index?page=0 처럼 GET 방식으로 요청된 URL에서 page값을 가져오기 위해
    // @RequestParam(value="page", defaultValue="0") int page가 list 메서드에 추가되었다.
    // URL에 페이지 파라미터 page가 전달되지 않은 경우 디폴트 값으로 0이 되도록 설정
    public String index(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        //questionService 객체는 생성자 방식으로 DI 규칙에 의해 주입
        model.addAttribute("paging", paging);
        return "index";
    }


    // id로 question 상세 페이지
    @GetMapping(value = "/question/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    // question_form.html 템플릿은 "질문 등록하기" 버튼을 통해 GET 방식으로 요청되더라도
    // th:object에 의해 QuestionForm 객체가 필요하기 때문에 여기도 QuestionForm questionForm
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }


    // 이거때문에 postmapping <form method="post">
    // 새 글 쓰기
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    // 이전 메서드명과 동일하게 사용가능. (매개변수의 형태가 다른 경우에만 -  오버로딩)
    // @Valid 애너테이션 붙여야 검증기능 작동
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {    //입력값 검증
            return "question_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());
        //subject, content 항목을 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩됨
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/";
    }

    // 글 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }


    //게시글수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/%s", id);
    }


    //게시글삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }





    //회원가입
    @GetMapping("/user/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/user/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        userService.create(userCreateForm.getUsername(), userCreateForm.getPassword());

        return "redirect:/";
    }

    //로그인 담당
    @GetMapping("/user/login")
    public String login() {
        return "login_form";
    }




}
