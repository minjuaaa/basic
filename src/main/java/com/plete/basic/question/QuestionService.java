package com.plete.basic.question;

import com.plete.basic.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.plete.basic.DataNotFoundException;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    //// 정수 타입의 페이지번호를 입력받아 해당 페이지의 질문 목록을 리턴
    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));   //역순으로 보여줌(최신순)
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));//(조회할페이지번호, 게시물갯수)
        return this.questionRepository.findAll(pageable);
        //questionRepository 객체는 생성자 방식으로 DI 규칙에 의해 주입된다
    }



    // 질문 상세화면 진입시 띄우는 내용...id 값으로 Question 데이터를 조회
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) { // isPresent 메서드로 해당 데이터가 존재하는지 확인
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문작성
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setAuthor(user);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
}
