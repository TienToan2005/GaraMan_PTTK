/* GDInputInfo6: nhap thong tin ca nhan, xac nhan dat lich (goi DoSaveAppointment6 qua POST /api/appointments) */
const booking = JSON.parse(sessionStorage.getItem('booking') || 'null');
if (!booking) location.href = 'GDSelectTime6.html';

const msg = document.getElementById('msg');
const form = document.getElementById('form');

requireRole('CUSTOMER').then(function (me) {
  document.getElementById('name').value = me.fullName || '';
  document.getElementById('phone').value = me.phone || '';
});
document.getElementById('when').textContent = booking ? booking.time + ' - ' + fmtDate(booking.date) : '';

const PHONE = /^0\d{9}$/;
const PLATE = /^[0-9]{2}[A-Za-z][A-Za-z0-9]?[- ]?[0-9]{3,5}(\.[0-9]{2})?$/;

form.addEventListener('submit', async function (e) {
  e.preventDefault();
  hide(msg);
  const name = document.getElementById('name').value.trim();
  const phone = document.getElementById('phone').value.trim();
  const plate = document.getElementById('plate').value.trim();
  if (!name || !phone || !plate) { showError(msg, 'Vui lòng nhập đủ họ tên, số điện thoại và biển số xe'); return; }
  if (!PHONE.test(phone)) { showError(msg, 'Số điện thoại không đúng định dạng (10 chữ số, bắt đầu bằng 0)'); return; }
  if (!PLATE.test(plate)) { showError(msg, 'Biển số xe không đúng định dạng (ví dụ 30A-123.45)'); return; }
  const btn = document.getElementById('submit');
  btn.disabled = true;
  try {
    const r = await api('/api/appointments', {
      method: 'POST',
      body: { date: booking.date, time: booking.time, name: name, phone: phone, licensePlate: plate }
    });
    document.getElementById('rId').textContent = '#' + r.id;
    document.getElementById('rWhen').textContent = r.time + ' - ' + fmtDate(r.date);
    document.getElementById('rPlate').textContent = r.licensePlate;
    document.getElementById('rName').textContent = r.contactName;
    document.getElementById('rStatus').textContent = r.status;
    document.getElementById('formCard').classList.add('hidden');
    document.getElementById('doneCard').classList.remove('hidden');
    sessionStorage.removeItem('booking');
  } catch (err) {
    showError(msg, err.message);
    btn.disabled = false;
  }
});
