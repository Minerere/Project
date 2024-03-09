package com.learncode.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learncode.Entity.DiscountCode;
import com.learncode.Entity.User;
import com.learncode.Sv.CartSv;
import com.learncode.Sv.DiscountCodeSv;
import com.learncode.Sv.OrderSv;
import com.learncode.Sv.UserSv;
import com.learncode.Utils.MailUtils;
import com.learncode.Utils.SessionUtil;
import com.learncode.dto.Cart;
import com.learncode.dto.Order;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api") // remove "/order" chuyen xuong anotation dong 40 & 60 ~~~ @Sang -> Muc dich: mo rong api
public class OrderApi {
	@Autowired
	private CartSv cartSv;
	@Autowired
	private UserSv userSv;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private OrderSv orderSv;
	
	@Autowired
	private MailUtils mailUtils;


	@Autowired
	DiscountCodeSv discountCodeSv; // bo sung khai bao lop service cua discountcode @Sang

	@PostMapping("/order") // da sua doi @Sang
	public ResponseEntity<Order> order(Order order, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		User user = ((User) session.getAttribute("user"));
		if (cartSv.get(user.getId()) == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		/* Bo sung @Sang */
		if (order.getDiscountCode() != "" && checkMethod(order.getDiscountCode()) == null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		Cart cart = cartSv.get(user.getId());
		int totalpayment = cart.getTotalAmount();
		
		orderSv.convert(cart, order);
		
		cartSv.remove(user.getId());
		//send email
		String to = user.getEmail();
		String subject = "Đơn đặt hàng của bạn đã được xử lý";
		String body = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Document</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "    <h3 style=\"color: blue;\">Đơn hàng của bạn đang được xử lý</h3>\r\n"
				+ "    <div>Tên khách hàng : "+user.getUsername()+"</div>\r\n"
				+ "    <div>Địa chỉ nhận hàng : "+user.getDeliveryAddress()+"</div>\r\n"
			    + "    <div>Quý khách vui lòng chuẩn bị đủ số tiền : "+totalpayment+"đ</div>\r\n"
			    + "    <h3 style=\"color: blue;\">Vui lòng chuẩn bị số tiền trên khi nhận hàng <3 </h3>\r\n"
				+ "    <h3 style=\"color: blue;\">MasterBee chân thành cảm ơn quý khách rất nhiều <3 </h3>\r\n"
				+ "    <div>Đây là email tư dộng, vui lòng không trả lời email này!</div>\r\n"
				+" <img src=\"/User/image/logo/logo-M.png\" alt=\"Logo MasterBee\">"
				+ "</body>\r\n"
				+ "</html>";
		
		
		try {
			mailUtils.sendEmailOrderSuccessfully(to, subject, body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		SessionUtil.refreshSession(httpSession, userSv);
		return new ResponseEntity<Order>(new Order(), HttpStatus.OK);
	}

	@DeleteMapping("/order")
	public void deleteOrder(@RequestBody Long[] ids) {

	}

	/* bo sung api @Sang */
	@PostMapping("/discount-check")
	public ResponseEntity<DiscountCode> check(String discountCode) {
		if(checkMethod(discountCode) != null) {
			SessionUtil.refreshSession(httpSession, userSv);
			return ResponseEntity.ok(checkMethod(discountCode));
		} else {
			SessionUtil.refreshSession(httpSession, userSv);
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	public DiscountCode checkMethod(String discountCode) {
		if (discountCodeSv.findByCode(discountCode) != null) {
			DiscountCode d = discountCodeSv.findByCode(discountCode);
			LocalDate today = LocalDate.now();
			if ((d.getEffectiveDate().isBefore(today) || d.getEffectiveDate().isEqual(today)) &&
					(d.getExpirationDate().isAfter(today) || d.getExpirationDate().isEqual(today))) {
				return d;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
