/* GDSelectDate6: chon ngay bat dau, ngay ket thuc roi chuyen sang GDStatSupplier6 */
requireRole('MANAGER');

const startEl = document.getElementById('start');
const endEl = document.getElementById('end');
const msg = document.getElementById('msg');
const p = new URLSearchParams(location.search);
// mac dinh: thang co du lieu mau trong kich ban (05/2023)
startEl.value = p.get('start') || '2023-05-01';
endEl.value = p.get('end') || '2023-05-31';

document.getElementById('form').addEventListener('submit', function (e) {
  e.preventDefault();
  hide(msg);
  if (!startEl.value || !endEl.value) { showError(msg, 'Vui lòng chọn ngày bắt đầu và ngày kết thúc'); return; }
  if (endEl.value < startEl.value) {
    showError(msg, 'Khoảng thời gian không hợp lệ (ngày kết thúc nhỏ hơn ngày bắt đầu)');
    return;
  }
  location.href = 'GDStatSupplier6.html?start=' + startEl.value + '&end=' + endEl.value;
});
