const groups = [
  { id: "1", content: "Conference Room 1" },
  { id: "2", content: "Conference Room 2" },
  { id: "3", content: "Conference Room 3" },
  { id: "4", content: "Conference Room 4" },
  { id: "5", content: "Conference Room 5" },
];

const weekDay = ["일", "월", "화", "수", "목", "금", "토"];

let today = new Date();
let startTime = setTime(today, 9, 0);
let endTime = setTime(today, 18, 0);

let events = [
  {
    id: "1",
    group: "1",
    content: "팀 회의",
    start: new Date("2025-02-18T10:00:00"),
    end: new Date("2025-02-18T11:00:00"),
    employee: "한민정",
  },
  {
    id: "2",
    group: "1",
    content: "화면 설계 회의",
    start: new Date("2025-02-18T11:10:00"),
    end: new Date("2025-02-18T13:00:00"),
    employee: "한민정",
  },
  {
    id: "3",
    group: "2",
    content: "디자인 리뷰",
    start: new Date("2025-02-18T13:30:00"),
    end: new Date("2025-02-18T15:00:00"),
    employee: "한민정",
  },
];

const container = document.getElementById("timeline");
const modal = document.getElementById("myModal");

const options = {
  stack: false,
  editable: true,
  moveable: false,
  timeAxis: { scale: "minute", step: 30 },
  orientation: { axis: "top" },
  start: startTime,
  end: endTime,
  margin: { axis: 0, item: { horizontal: 0, vertical: 0 } },
  showTooltips: true,
  onAdd: handleAddEvent,
  onUpdate: handleUpdateEvent,
  onMove: handleMoveEvent,
  onRemove: function (item) {
    handleRemoveEvent(item.id);
  },
};

const timeline = new vis.Timeline(container, events, groups, options);

document.addEventListener("DOMContentLoaded", function () {
  modal.style.display = "none";
  window.onclick = (event) => {
    if (event.target == modal) modal.style.display = "none";
  };
  showData();
});

document.getElementById("reservationForm").onsubmit = function (event) {
  event.preventDefault();
  const newEvent = getEventDataFromForm();

  if (!isEndTimeValid(newEvent.end)) {
    alert("종료일은 18시 이전까지 가능합니다. ");
    return;
  }

  handleEventSubmit(newEvent);
  showData();
};

function handleAddEvent(item) {
  onDoubleClick(item);
}

function handleUpdateEvent(item) {
  onDoubleClick(item, item.id);
}

function handleMoveEvent(item) {
  const eventIdx = getEventIndexById(item.id);

  if (!isTimeSlotAvailable(item)) {
    alert("이미 해당 시간에 예약된 회의가 있습니다!");
  } else if (!isEndTimeValid(item.end)) {
    alert("종료일은 18시 이전까지 가능합니다. ");
  } else {
    events[eventIdx].group = item.group;
    events[eventIdx].start = item.start;
    events[eventIdx].end = item.end;
  }

  timeline.setItems(events);
  showData();
}

function handleRemoveEvent(id) {
  if (!confirm("정말로 이 예약을 삭제하시겠습니까?")) return;

  const eventIdx = getEventIndexById(id);
  events.splice(eventIdx, 1);
  timeline.setItems(events);
  showData();
}

function onDoubleClick(item, id) {
  modal.style.display = "block";
  openModal(item, id);
}

function openModal(item, id) {
  const groupName = getGroupNameById(item.group);
  if (id) {
    const event = events.find((e) => e.id == id);
    setModalForm(event, groupName);
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

function setModalForm(event, groupName) {
  document.getElementById("id").value = event.id || "";
  document.getElementById("group").value = event.group || "";
  document.getElementById("content").value = event.content || "";
  document.getElementById("resource_name").value = groupName;
  document.getElementById("start-time").value = formatDate(event.start);

  const endTime = new Date(event.start);
  endTime.setMinutes(endTime.getMinutes() + 60);

  if (!isEndTimeValid(endTime)) {
    endTime.setHours(18, 0, 0);
  }

  document.getElementById("end-time").value = formatDate(endTime);

  const yyyy = event.start.getFullYear();
  const mm = (event.start.getMonth() + 1).toString().padStart(2, "0");
  const dd = event.start.getDate().toString().padStart(2, "0");
  const date = `${yyyy}-${mm}-${dd}`;

  document.getElementById("start-date").textContent = date;
  document.getElementById("end-date").textContent = date;
}

function isEndTimeValid(endTime) {
  if (endTime.getHours() === 18 && endTime.getMinutes() > 0) {
    return false;
  }
  return true;
}

function getEventIndexById(id) {
  for (let i = 0; i < events.length; i++) {
    if (events[i].id == id) {
      return i;
    }
  }
  return -1;
}

function getEventDataFromForm() {
  const startDate = document.getElementById("start-date").innerText;
  const endDate = document.getElementById("end-date").innerText;
  const startTime = document.getElementById("start-time").value;
  const endTime = document.getElementById("end-time").value;

  return {
    id: document.getElementById("id").value || events.length + 1,
    group: document.getElementById("group").value,
    content: document.getElementById("content").value,
    start: new Date(`${startDate} ${startTime}`),
    end: new Date(`${endDate} ${endTime}`),
    employee: "한민정",
  };
}

function isTimeSlotAvailable(newEvent) {
  for (let i = 0; i < events.length; i++) {
    const event = events[i];

    if (
      event.group === newEvent.group &&
      event.id != newEvent.id &&
      ((newEvent.start >= event.start && newEvent.start < event.end) ||
        (newEvent.end > event.start && newEvent.end <= event.end) ||
        (newEvent.start <= event.start && newEvent.end >= event.end))
    ) {
      return false;
    }
  }
  return true;
}

function handleEventSubmit(newEvent) {
  if (!isTimeSlotAvailable(newEvent)) {
    alert("이미 해당 시간에 예약된 회의가 있습니다!");
    return;
  }

  const eventIdx = getEventIndexById(newEvent.id);
  if (eventIdx !== -1) {
    events[eventIdx] = newEvent;
  } else {
    events.push(newEvent);
  }
  timeline.setItems(events);
  modal.style.display = "none";
}

function showData() {
  const tbody = document.getElementById("reservation-tbody");
  let html = "";

  for (let i = 0; i < events.length; i++) {
    const currentEvent = events[i];
    const groupName = getGroupNameById(currentEvent.group);
    html += `<tr>
                <td>${currentEvent.id}</td>
                <td>${currentEvent.employee}</td>
                <td>${currentEvent.content}</td>
                <td>${formatDateToKor(currentEvent.start, true)}</td>
                <td>${formatDateToKor(currentEvent.end, true)}</td>
                <td>${groupName}</td>
                <td>${
                  isEventOngoing(currentEvent)
                    ? `<button>반납</button>`
                    : `<button onclick="handleRemoveEvent(${currentEvent.id})">취소</button>`
                }</td>
              </tr>`;
  }

  tbody.innerHTML = html;
}

function isEventOngoing(event) {
  const currentTime = new Date();
  const eventStart = new Date(event.start);
  const eventEnd = new Date(event.end);

  return currentTime >= eventStart && currentTime <= eventEnd;
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
  const options = {
    start: startTime,
    end: endTime,
    editable: !isWeekend(today),
  };

  timeline.setOptions(options);
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

function formatDateToKor(date, withTime) {
  let text = `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;

  if (withTime) {
    text += ` ${date.getHours()}:${String(date.getMinutes()).padStart(2, "0")}`;
  }

  text += ` (${weekDay[date.getDay()]})`;
  return text;
}

function formatDate(date) {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  const hours = String(d.getHours()).padStart(2, "0");
  const minutes = String(d.getMinutes()).padStart(2, "0");
  return `${hours}:${minutes}`;
}
