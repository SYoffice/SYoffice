const groups = [];
let tl_reservations = [];
let temp_curPage = 1;

const weekDay = ["일", "월", "화", "수", "목", "금", "토"];

// 타임라인의 날짜
let today = new Date();
let startTime = setTime(today, 9, 0);
let endTime = setTime(today, 18, 0);

const container = document.getElementById("timeline");
const modal = document.getElementById("myModal");

const options = {
	stack: false,
	editable: !isWeekend(today),
	moveable: false,
	timeAxis: { scale: "minute", step: 30 },
	orientation: { axis: "top" },
	start: startTime,
	end: endTime,
	margin: { axis: 0, item: { horizontal: 0, vertical: 0 } },
	showTooltips: true,
	onAdd: handleAddItem,
	onUpdate: handleUpdateItem,
	onMove: handleMoveItem,
	onRemove: function(item) {
		handleRemoveItem(item.id, item.fk_emp_id);
	},
};

const timeline = new vis.Timeline(container, tl_reservations, groups, options);

// 이 페이지의 카테고리 넘버
const menu_category_no = document.getElementById("category_no").value;
// 이 페이지에 접속한 사원 아이디
const login_user_id = document.getElementById("login_user_id").value;

document.addEventListener("DOMContentLoaded", function() {
	getResourceList();
	syncReservList();

	modal.style.display = "none";
	window.onclick = (event) => {
		if (event.target == modal) modal.style.display = "none";
	};
});

function syncReservList() {
	getReservationList();
	getReservationPageList(1);
}

document.getElementById("reservationForm").onsubmit = function(event) {
	event.preventDefault();
	const newItem = getItemDataFromForm();

	if (!isEndTimeValid(newItem.end)) {
		alert("종료일은 18시 이전까지 가능합니다. ");
		return;
	}

	handleItemSubmit(newItem);
};

function handleAddItem(item) {	
	onDoubleClick(item);
}

function isMyReserv(targetItem, fk_emp_id) {
	if (targetItem && targetItem.fk_emp_id != login_user_id) {
		// alert('수정은 오직 본인이 예약한 사항에 한하여 가능합니다.');
		return false;
	}
	if (fk_emp_id && fk_emp_id != login_user_id) {
		return false;
	}
	return true;
}

function handleUpdateItem(item) {
	const itemIdx = getItemIndexById(item.id);
	const targetItem = tl_reservations[itemIdx];

	if (isMyReserv(targetItem)) {
		onDoubleClick(item, item.id);
	}

	updateTimeline();

}

function handleMoveItem(item) {

	const itemIdx = getItemIndexById(item.id);

	if (!isMyReserv(item) || !isTimeSlotAvailable(item)) {
		updateTimeline();
		return;
	} else if (!isEndTimeValid(item.end)) {
		alert("종료일은 18시 이전까지 가능합니다. ");
	} else {
		tl_reservations[itemIdx].group = item.group;
		tl_reservations[itemIdx].start = item.start;
		tl_reservations[itemIdx].end = item.end;
		updateReservation(item)
	}
}

function handleRemoveItem(id, fk_emp_id) { // fk_emp_id 예약한 사람
	if (!isMyReserv(null, fk_emp_id)) return;

	if (!confirm("정말로 이 예약을 삭제하시겠습니까?")) return;

	removeReservation(id);
}


function handleReturnItem(id) {
	if (!confirm("정말로 이 자원을 반납하시겠습니까?")) return;

	removeReservation(id, true);
}


function onDoubleClick(item, id) {

	modal.style.display = "block";

	openModal(item, id);
}

function openModal(item, id) {
	console.log('openModal:', item, id)
	const groupName = getGroupNameById(item.group);
	if (id) {
		const item = tl_reservations.find((e) => e.id == id);
		setModalForm(item, groupName);
	} else {
		setModalForm({ group: item.group, start: item.start }, groupName);
	}
}

function getGroupNameById(groupId) {
	for (let i = 0; i < groups.length; i++) {
		if (groups[i].id == groupId) {
			return groups[i].content;
		}
	}
	return "";
}

function setModalForm(item, groupName) {
	document.getElementById("id").value = item.id || "";
	document.getElementById("group").value = item.group || "";
	document.getElementById("empl_name").value = item.empl_name || "";
	document.getElementById("resource_name").value = groupName;
	document.getElementById("start-time").value = formatTime(item.start);

	const endDate = new Date(item.end);
	if (!item.end) {
		endDate.setMinutes(endDate.getMinutes() + 60);
	}

	if (!isEndTimeValid(endDate)) {
		endDate.setHours(18, 0, 0);
	}

	document.getElementById("end-time").value = formatTime(endDate);

	const targetDate = new Date(item.start);

	const yyyy = targetDate.getFullYear();
	const mm = (targetDate.getMonth() + 1).toString().padStart(2, "0");
	const dd = targetDate.getDate().toString().padStart(2, "0");
	const date = `${yyyy}-${mm}-${dd}`;

	document.getElementById("start-date").textContent = date;
	document.getElementById("end-date").textContent = date;
}


function isEndTimeValid(endTime) {
   // 가능한 마지막 시간대: 18시 00분
   const _today_endtime = today;
   _today_endtime.setHours(18); // 18시
   _today_endtime.setMinutes(0); // 00분
      
   if (endTime > _today_endtime) {
      return false;
   }
   return true;
}

function getItemIndexById(id) {
	for (let i = 0; i < tl_reservations.length; i++) {
		if (tl_reservations[i].id == id) {
			return i;
		}
	}
	return -1;
}

function getItemDataFromForm() {
	const startDate = document.getElementById("start-date").innerText;
	const endDate = document.getElementById("end-date").innerText;
	const startTime = document.getElementById("start-time").value;
	const endTime = document.getElementById("end-time").value;
	const empl_name = document.getElementById("empl_name").value;

	return {
		id: document.getElementById("id").value || null,
		group: document.getElementById("group").value,
		start: new Date(`${startDate} ${startTime}`),
		end: new Date(`${endDate} ${endTime}`),
		empl_name: empl_name
	};
}

function isTimeSlotAvailable(newItem) {
	for (let i = 0; i < tl_reservations.length; i++) {
		const item = tl_reservations[i];

		// 예약이 같은 자원(group)일 경우에만 비교
		if (item.group === newItem.group && item.id !== newItem.id) {
			// 겹침 조건:
			// 1. newItem가 item 시작 전에 시작해서 끝나기 전에 끝날 경우
			// 2. newItem가 item 시작 후에 시작해서 끝날 때 item 끝난 후에 끝날 경우
			// 3. newItem가 item 시작 후에 시작해서 item 끝난 후에 끝날 경우

			if (new Date(newItem.start) < new Date(item.end) && new Date(newItem.end) > new Date(item.start)) {
				// 중간에 겹치는 경우
				alert("이미 예약 내역이 있습니다.");
				return false;
			}

			else if (new Date(newItem.start) > new Date(newItem.end)) {
				// 시작시간이 종료시간보다 앞선 경우
				alert("시작시간이 종료시간보다 앞서야 합니다.");
				return false;
			}
		}
	}
	return true;
}


function handleItemSubmit(newItem) {
	const itemIdx = getItemIndexById(newItem.id);

	console.log(itemIdx, newItem);

	if (!isTimeSlotAvailable(newItem)) {
		//   alert("이미 예약된 회의가 있거나 시작시간이 종료시간보다 앞서야 합니다.");
		return;
	}


	if (itemIdx !== -1) {
		tl_reservations[itemIdx] = newItem;
		updateReservation(newItem);
	} else {
		addReservation(newItem);
		tl_reservations.push(newItem);
	}
	updateTimeline();
	modal.style.display = "none";
}

function prevDate() {
	changeDate(-1);
}

function nextDate() {
	changeDate(1);
}

function setToday() {
	today = new Date();
	startTime = setTime(today, 9, 0);
	endTime = setTime(today, 18, 0);
	updateTimeline();
}

function updateTimeline() {
	options.start = startTime;
	options.end = endTime;
	options.editable = !isWeekend(today) && today > new Date();

	timeline.setGroups(new vis.DataSet(groups));
	timeline.setOptions(options);
	timeline.setItems(tl_reservations);
}

function changeDate(offset) {
	today.setDate(today.getDate() + offset);
	startTime = setTime(today, 9, 0);
	endTime = setTime(today, 18, 0);
	updateTimeline();
}

function setTime(date, hours, minutes) {
	document.getElementById("today-text").innerText = formatDateToKor(today);
	return new Date(date.setHours(hours, minutes, 0, 0));
}

function isWeekend(date) {
	const day = date.getDay();
	return day === 0 || day === 6;
}

function formatDateToKor(target, withTime) {
	const date = new Date(target);
	let text = `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;

	if (withTime) {
		text += ` ${date.getHours()}:${String(date.getMinutes()).padStart(2, "0")}`;
	}

	text += ` (${weekDay[date.getDay()]})`;
	return text;
}

function formatTime(date) {
	const d = new Date(date);
	const hours = String(d.getHours()).padStart(2, "0");
	const minutes = String(d.getMinutes()).padStart(2, "0");
	return `${hours}:${minutes}`;
}

function formatFullDate(date) {
	const d = new Date(date);
	const year = d.getFullYear();
	const month = String(d.getMonth() + 1).padStart(2, "0");
	const day = String(d.getDate()).padStart(2, "0");
	const hours = String(d.getHours()).padStart(2, "0");
	const minutes = String(d.getMinutes()).padStart(2, "0");
	return `${year}-${month}-${day} ${hours}:${minutes}`;
}


// 예약 조회 함수 - 타임라인 세팅
function getReservationList() {
	// 초기화
	tl_reservations = [];
	$.ajax({
		url: "/syoffice/reservation/selectReservationList",
		type: "GET",
		data: { "category_no": menu_category_no },
		dataType: "json",
		success: function(json) {

			if (json.length > 0) {
				$.each(json, function(_index, item) {
					// 전역 timeline 라이브러리 데이터 값에 넣기
					tl_reservations.push({
						id: item.reserv_no,
						group: item.fk_resource_no,
						content: `${item.empl_name} ${item.reserv_start} - ${item.reserv_end}`,
						start: new Date(item.reserv_start),
						end: new Date(item.reserv_end),
						empl_name: item.empl_name,
						fk_emp_id: item.fk_emp_id,
						editable: isMyReserv(item)
					});
				});
				// 업데이트 타임라인
				updateTimeline();

			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

// 예약 조회 함수 - 페이징
function getReservationPageList(curPage) {
	$.ajax({
		url: "/syoffice/reservation/selectReservationList",
		type: "GET",
		data: { "category_no": menu_category_no, "curPage": curPage },
		dataType: "json",
		success: function(json) {
			console.log('페이징 예약 조회 ::', json);
			temp_curPage = curPage;
			const tbody = document.getElementById("reservation-tbody");
			let html = "";

			if (json.length === 0) {
				html += `<tr class="empty-table"><td colspan="6">예약 데이터가 없습니다.</td></tr>`
			}

			if (json.length > 0) {
				$.each(json, function(_index, item) {
					html += `<tr>
                            <td>${item.reserv_no}</td>
                            <td>${item.empl_name}</td>
                            <td>${formatDateToKor(item.reserv_start, true)}</td>
                            <td>${formatDateToKor(item.reserv_end, true)}</td>
                            <td>${item.resource_name}</td>`;

					// 예약의 fk_emp_id 와 로그인한 유저 아이디가 같으면 버튼 노출
					if (login_user_id == item.fk_emp_id && new Date(item.reserv_end) > new Date()) {
						html += `<td>
                           ${item.posibleCancel ? `<button onclick="handleReturnItem(${item.reserv_no}, ${item.fk_emp_id})">반납</button>` : `<button onclick="handleRemoveItem(${item.reserv_no}, ${item.fk_emp_id})">취소</button>`}
                        </td>`;
					} else {
						html += `<td style="text-align: center"> 사용 완료 </td>`;
					}

					html += `</tr>`;
				});

			}
			tbody.innerHTML = html;

			// 페이지네이션 갱신
			updatePagination();
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

// 페이지네이션을 동적으로 갱신하는 함수
function updatePagination() {
	// 다른 애들은 active 제거
	for (let item of document.getElementsByClassName("pageNo")) {
		item.classList.remove("active");
	}

	// 페이지네이션 업데이트
	document.getElementById(`pageNoIdx${temp_curPage}`).classList.add("active");
}

// 리소스(자원) 조회 함수
function getResourceList() {
	$.ajax({
		url: "/syoffice/reservation/selectResourceList",
		type: "GET",
		data: { "category_no": menu_category_no },
		dataType: "json",
		success: function(json) {
			console.log('자원 조회 ::', json);
			$.each(json, function(_index, item) {
				groups.push({ id: item.resource_no, content: item.resource_name });
			});
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

// 예약 등록 함수
function addReservation(reservation) {
	const newReservation = {
		reserv_start: formatFullDate(reservation.start),
		reserv_end: formatFullDate(reservation.end),
		fk_resource_no: reservation.group
	}
	console.log("addReservation :: ", reservation, newReservation)

	$.ajax({
		url: "/syoffice/reservation/insert",
		type: "POST",
		data: newReservation,
		success: function(res) {
			if (res == 1) {
				alert("예약이 등록되었습니다");
				syncReservList();
			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

// 예약 수정 함수
function updateReservation(reservation) {
	const newReservation = {
		reserv_no: reservation.id,
		reserv_start: formatFullDate(reservation.start),
		reserv_end: formatFullDate(reservation.end),
		fk_resource_no: reservation.group
	}

	console.log(newReservation)

	$.ajax({
		url: "/syoffice/reservation/update",
		type: "PUT",
		data: newReservation,
		success: function(res) {
			if (res == 1) {
				alert("예약이 수정되었습니다");
				syncReservList();
			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}

// 예약 취소 함수
function removeReservation(id) {
	$.ajax({
		url: "/syoffice/reservation/delete",
		type: "DELETE",
		data: { "reserv_no": id },
		dataType: "json",
		success: function(res) {
			if (res == 1) {
				alert("예약이 취소되었습니다");
				syncReservList();
			}
		},
		error: function(request, status, error) {
			alert("code: " + request.status + "\n" + "message: " + request.responseText + "\n" + "error: " + error);
		}
	});
}