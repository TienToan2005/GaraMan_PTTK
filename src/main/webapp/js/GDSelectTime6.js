/* GDSelectTime6: chon ngay hen va khung gio con trong */
requireRole('CUSTOMER');

const dateInput = document.getElementById('date');
const slotsBox = document.getElementById('slots');
const nextBtn = document.getElementById('next');
const msg = document.getElementById('msg');
let selectedTime = null;

dateInput.min = todayIso();
const saved = JSON.parse(sessionStorage.getItem('booking') || 'null');
if (saved) dateInput.value = saved.date;

async function loadSlots() {
  hide(msg);
  selectedTime = null;
  nextBtn.disabled = true;
  slotsBox.innerHTML = '';
  if (!dateInput.value) return;
  try {
    const slots = await api('/api/slots?date=' + encodeURIComponent(dateInput.value));
    slots.forEach(function (s) {
      const b = document.createElement('button');
      b.type = 'button';
      b.className = 'slot';
      b.textContent = s.time;
      b.disabled = !s.available;
      b.title = s.available ? '' : 'Khung giờ đã kín';
      b.addEventListener('click', function () {
        selectedTime = s.time;
        slotsBox.querySelectorAll('.slot').forEach(function (x) { x.classList.remove('selected'); });
        b.classList.add('selected');
        nextBtn.disabled = false;
      });
      if (saved && saved.date === dateInput.value && saved.time === s.time && s.available) b.click();
      slotsBox.appendChild(b);
    });
  } catch (err) {
    showError(msg, err.message);
  }
}

dateInput.addEventListener('change', loadSlots);
nextBtn.addEventListener('click', function () {
  if (!dateInput.value || !selectedTime) {
    showError(msg, 'Vui lòng chọn ngày và giờ hẹn');
    return;
  }
  sessionStorage.setItem('booking', JSON.stringify({ date: dateInput.value, time: selectedTime }));
  location.href = 'GDInputInfo6.html';
});
if (dateInput.value) loadSlots();
