package net.uzen.study.controller;

import lombok.RequiredArgsConstructor;
import net.uzen.study.domain.Address;
import net.uzen.study.domain.Member;
import net.uzen.study.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 화면
     *
     * @param model
     * @return
     */
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 회원가입 처리
     * <p>만약 에러가 발생하면, BindingResult 에 에러가 같이 담겨서 오게 되는데, result.hasErrors() 로 에러 유무을 판단해서 처리한다.
     * <p>화면에 값을 넘길 때, Member 를 사용하지 않고, MemberForm 을 사용하는 이유는 화면에서 처리하는 데이터에 최적화하기 위해서 별도로 Form 을 만들어서 처리한 것이다.
     * <p>Member 를 사용해도 상관 없으나, 엔티티가 지저분해지거나, 각 연할마다 validation 이 다를 수 있고, 불필요한 정보가 들어있을 수 있기 때문에 Form 을 별도 생성하여 처리한다.
     *
     * @param memberForm
     * @param result
     * @return
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {

        // 에러가 발생하면, 메시지를 화면에 보여준다.
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()));

        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 회원 목록 조회
     *
     * @param model
     * @return
     */
    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}