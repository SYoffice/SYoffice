
function handleReturnItem(id) {
	if (!confirm("정말로 이 자원을 반납하시겠습니까?")) return;

	removeReservation(id, true);
}


function handleRemoveItem(id) {
	if (!confirm("정말로 이 예약을 삭제하시겠습니까?")) return;

	removeReservation(id);
}

// isReturn 반납인지 아닌지 
function removeReservation(id, isReturn) {
	$.ajax({
		url: "/syoffice/reservation/delete",
		type: "DELETE",
		data: { "reserv_no": id },
		dataType: "json",
		success: function(res) {
			if (res == 1) {
				alert(`예약이 ${isReturn ? '반납' : '취소'}되었습니다`);
				location.href = location.href;
			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}
