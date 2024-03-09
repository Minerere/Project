$(document).ready(function () {
	$(".home-slider").owlCarousel({
		items: 1,  // Số lượng hiển thị của mỗi slide
		loop: true, // Lặp lại slide
		autoplay: true, // Tự động chạy slide
		autoplayTimeout: 1500, // Thời gian hiển thị mỗi slide (ms)
		autoplayHoverPause: true, // Tạm dừng tự động chạy khi rê chuột qua slide
		animateOut: 'fadeOut', // Hiệu ứng khi chuyển đi (sử dụng animate.css)
		animateIn: 'fadeIn', // Hiệu ứng khi chuyển đến (sử dụng animate.css)
	});
	$(".slide2").owlCarousel({
		items: 4, // Số lượng hiển thị sản phẩm trong mỗi slide
		loop: true, // Lặp lại slide
		margin: 10, // Khoảng cách giữa các sản phẩm
		autoplay: true, // Tự động chạy slide
		autoplayTimeout: 2000, // Thời gian hiển thị mỗi slide (ms)
		autoplayHoverPause: true, // Tạm dừng tự động chạy khi rê chuột qua slide
	});
});

window.addEventListener("scroll", function () {
	const navbar = document.getElementById("sticky-nav");
	if (window.scrollY > 50) {
		navbar.classList.remove('light')
		navbar.classList.add('dark')
	} else {
		navbar.classList.remove('dark')
		navbar.classList.add('light')
	}
});

document.addEventListener("DOMContentLoaded", function () {
	const menuItems = document.querySelectorAll(".menu-item");
	const overlay = document.querySelector(".overlay");

	function showOverlay() {
		overlay.style.display = 'block';
	}
	function hideOverlay() {
		overlay.style.display = 'none';
	}
	menuItems.forEach(function (menuItem) {
		menuItem.addEventListener("mouseenter", showOverlay);
		menuItem.addEventListener("mouseleave", hideOverlay);
	});
});
$('.buy-now').off('click').on('click', function (e) {
	e.preventDefault();
	var btn = $(this);
	var id = btn.data('id');
	var quantity = btn.data('quantity');
	$.ajax({
		url: "/api/cart",
		dataType: "json",
		method: "POST",
		data: {
			product: id,
			quantity: quantity
		},
		success: (rs) => {
			Swal.fire({
				icon: 'success',
				title: 'Thêm sản phẩm vào giỏ hàng thành công',
				showConfirmButton: false,
				timer: 1000
			});
		},
		error: (rs) => {
			if (rs.status == 401) {
				Swal.fire({
					icon: 'warning',
					title: 'Phải đăng nhập trước khi thực hiện hành động này',
					showConfirmButton: false,
					timer: 2000
				});
				setTimeout(() => {
					document.location.href = "/login";
				}, 2000);
			} else
				console.log(rs);
		}
	});
});
$('#add-to-cart').off('click').on('click', function (e) {
	e.preventDefault();
	var btn = $(this);
	var id = btn.data('id');
	var quantity = btn.data('quantity');
	$.ajax({
		url: "/api/cart",
		dataType: "json",
		method: "POST",
		data: {
			product: id,
			quantity: quantity
		},
		success: (rs) => {
			Swal.fire({
				icon: 'success',
				title: 'Thêm sản phẩm vào giỏ hàng thành công',
				showConfirmButton: false,
				timer: 1000
			});
		},
		error: (rs) => {
			if (rs.status == 401) {
				Swal.fire({
					icon: 'warning',
					title: 'Đăng nhập trước khi thêm vào giỏ hàng',
					showConfirmButton: false,
					timer: 2000
				});
				setTimeout(() => {
					document.location.href = "/login";
				}, 2000);
			}
			else if(rs.status == 400) {
				Swal.fire({
					icon: 'warning',
					title: 'Số lượng sản phẩm trong giỏ hàng không được vượt quá số lượng trong kho',
					showConfirmButton: false,
					timer: 2000
				});
			}
			 else
				console.log(rs);
		}
	});
});

$('#quantity').on('change', function (e) {
	$('#add-to-cart').attr('data-quantity', $('#quantity').val());
});



$('#clear-cart-all').off('click').on('click', function (e) {
	e.preventDefault();

	Swal.fire({
		title: 'Confirm deletion?',
		text: "Bạn có chắc chắn muốn xóa tất cả sản phẩm trong giỏ hàng?",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Xác nhận '
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: "/api/cart/all",
				dataType: "json",
				method: "DELETE",
				success: (rs) => {
					Swal.fire({
						title: 'Xóa thành công!',
						text: 'Xóa thành công',
						showConfirmButton: false,
						timer: 2000
					});
					setTimeout(() => {
						document.location.href = "/cart";
					}, 2000);
				},
				error: (rs) => {
					if (rs.status == 401) {
						alert('Vui lòng đăng nhập trước khi xóa sản phẩm');
						document.location.href = "/login";
					} else
						if (rs.status == 200) {
							Swal.fire({
								title: 'Xóa thành công!',
								icon: 'success',
								text: 'Xóa sản phẩm thành công!',
								showConfirmButton: false,
								timer: 2000
							});
							setTimeout(() => {
								document.location.href = "/cart";
							}, 2000);
						} else
							console.log(rs);
				}
			});
		}
	});
});

// $('.quantity-cart').on('change', function(e) {
// 	e.preventDefault();
// 	console.log('a');
// 	var btn = $(this);
// 	var id = btn.data('id');
// 	var quantity = btn.val();
// 	$.ajax({
// 		url: "/api/cart",
// 		dataType: "json",
// 		method: "PUT",
// 		data: {
// 			product: id,
// 			quantity: quantity
// 		},
// 		success: (rs) => {
// 				$('#total').html(rs.totalAmount.toLocaleString('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 0, maximumFractionDigits: 0 }));
// 				console.log("Cập nhập giỏ hàng thành công!");
// 		},
// 		error: (rs) => {
// 			if (rs.status == 401) {
// 				Swal.fire({
// 					title: 'Warning!',
// 					text: 'Vui lòng đăng nhập trước khi cập nhập sản phẩm trong giỏ hàng!',
// 					showConfirmButton: false,
// 					timer: 2000
// 				});
// 				document.location.href = "/login";
// 			} else
// 				console.log(rs);
// 		}
// 	});
// });

$('#order').on('click', function (e) {
	// e.preventDefault();
	var address = $('#address').val();
	var tel = $('#tel').val();
	var discountCode = $('#discountCode').val();
	if (address.length <= 0 || tel.length <= 0) {

		Swal.fire({
			title: 'Cảnh báo!',
			text: 'Bạn phải cập nhật thông tin cá nhân trước khi đặt hàng',
			icon: 'warning',
			showConfirmButton: false,
			timer: 2000
		});
		setTimeout(function () {
			location.href = '/account/information'
		}, 2000);

	} else {
		$.ajax({
			url: "/api/order",
			dataType: "json",
			method: "POST",
			data: {
				deliveryAddress: address,
				tel: tel,
				discountCode: discountCode
			},
			success: (rs) => {
				Swal.fire({
					title: 'Success!',
					text: 'Đặt hàng thành công, bạn sẽ trở lại trang mua sắm!',
					icon: 'success',
					showConfirmButton: false,
					timer: 2000
				});
				setTimeout(() => {
					document.location.href = "/index";
				}, 2000);
			},
			error: (rs) => {
				if (rs.status == 401) {
					alert('Vui lòng đăng nhập trước khi đặt hàng!');
					document.location.href = "/login";
				} else if (rs.status == 406) {
					Swal.fire({
						title: 'Thất bại!',
						text: 'Đặt hàng thất bại, mã giảm giá không hợp lệ',
						icon: 'error',
						showConfirmButton: false,
						timer: 2000
					});
				} else {
					if (rs.status == 404)
						alert('Không có sản phẩm nào trong giỏ hàng!');
					else
						console.log(rs);
				}
			}
		});
	}
});
/* bo sung @Sang*/
function applyDiscountCode() {
	var discountCode = $('#discountCode').val();
	$.ajax({
		url: "/api/discount-check",
		dataType: "json",
		method: "POST",
		data: {
			discountCode: discountCode
		},
		success: (rs) => {
			//console.log(rs.value)
			$('#appliedDiscount').removeClass('d-none')
			$('#discountAmount').text('-' + rs.value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND', minimumFractionDigits: 0, maximumFractionDigits: 0 }))
			let totalPayment = parseInt($('#totalPayment').text().replaceAll('.', '').split(' ')[0]);
			totalPayment = totalPayment - rs.value;
			$('#totalPayment').text(totalPayment.toLocaleString('vi-VN', { style: 'currency', currency: 'VND', minimumFractionDigits: 0, maximumFractionDigits: 0 }))
			Swal.fire({
				title: 'Áp dụng mã giảm giá thành công',
				text: 'Bạn được giảm ' + rs.value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND', minimumFractionDigits: 0, maximumFractionDigits: 0 }),
				icon: 'success',
				showConfirmButton: false,
				timer: 2000
			});
		},
		error: (rs) => {
			if (rs.status == 406) {
				Swal.fire({
					title: 'Thất bại',
					text: 'Mã giảm giá không hợp lệ',
					icon: 'error',
					showConfirmButton: false,
					timer: 2000
				});
			} else {
				if (rs.status == 404)
					alert('Không tìm thấy sản phẩm nào trong giỏ hàng');
				else
					console.log(rs);
			}
		}
	});
}