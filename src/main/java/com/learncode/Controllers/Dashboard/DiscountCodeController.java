package com.learncode.Controllers.Dashboard;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.learncode.Entity.DiscountCode;
import com.learncode.Entity.User;
import com.learncode.Entity.UserRole;
import com.learncode.Sv.DiscountCodeSv;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DiscountCodeController {
    @Autowired
    private DiscountCodeSv discountCodeSv;

    @GetMapping("/discountCodeTable")
    public String table(HttpSession session, Model model,
            @RequestParam(value = "id", required = false) Optional<Integer> id,
            @RequestParam(value = "action", required = false) Optional<String> action) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == UserRole.ADMIN) {
            try {
                if (action.isPresent() && action.get().equals("remove") && id.isPresent()) {
                    discountCodeSv.deleteById(id.get());
                    model.addAttribute("success", "Xóa thành công");
                }
            } catch (Exception ex) {
                model.addAttribute("error", ex.getMessage());
            } finally {
                model.addAttribute("user", user);
                model.addAttribute("discountCodes", discountCodeSv.findAll());
            }
            return "Dashboard/discountCodeTable";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/addDiscountCode")
    public String add(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == UserRole.ADMIN) {
            model.addAttribute("user", user);
            return "Dashboard/addDiscountCode";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/updateDiscountCode/{id}")
    public String update(HttpSession session, Model model, @PathVariable(value = "id") int id) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == UserRole.ADMIN) {
            model.addAttribute("user", user);
            model.addAttribute("discountCode", discountCodeSv.findById(id));
            return "Dashboard/updateDiscountCode";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/discountCode/onAdd")
    public String onAdd(HttpSession session, Model model, RedirectAttributes redirectAttributes,
            @RequestParam String code,
            @RequestParam int value,
            @RequestParam LocalDate effectiveDate,
            @RequestParam LocalDate expirationDate) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == UserRole.ADMIN) {
            DiscountCode discountCode = new DiscountCode();
            discountCode.setCode(code);
            discountCode.setValue(value);
            discountCode.setEffectiveDate(effectiveDate);
            discountCode.setExpirationDate(expirationDate);
            discountCode.setCreatedAt(new Date());
            try {
                discountCodeSv.save(discountCode);
                redirectAttributes.addFlashAttribute("success", "Mã giảm giá được thêm thành công");
                return "redirect:/dashboard/discountCodeTable";
            } catch (Exception ex) {
                model.addAttribute("user", user);
                model.addAttribute("error", ex.getMessage());
                return "Dashboard/addDiscountCode";
            }
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/discountCode/onUpdate")
    public String onUpdate(HttpSession session, Model model, RedirectAttributes redirectAttributes,
            @RequestParam int id,
            @RequestParam String code,
            @RequestParam int value,
            @RequestParam LocalDate effectiveDate,
            @RequestParam LocalDate expirationDate) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == UserRole.ADMIN) {
            DiscountCode discountCode = discountCodeSv.findById(id);
            discountCode.setCode(code);
            discountCode.setValue(value);
            discountCode.setEffectiveDate(effectiveDate);
            discountCode.setExpirationDate(expirationDate);
            try {
                discountCodeSv.save(discountCode);
                redirectAttributes.addFlashAttribute("success", "Mã giảm giá được cập nhật thành công");
                return "redirect:/dashboard/discountCodeTable";
            } catch (Exception ex) {
                model.addAttribute("user", user);
                model.addAttribute("discountCode", discountCodeSv.findById(id));
                model.addAttribute("error", ex.getMessage());
                return "Dashboard/updateDiscountCode";
            }
        } else {
            return "redirect:/login";
        }
    }
}
