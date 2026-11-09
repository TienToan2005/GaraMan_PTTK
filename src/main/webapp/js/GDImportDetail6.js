/* GDImportDetail6: hoa don nhap chi tiet (thong tin chung, cac dong phu tung, tong tien) */
requireRole('MANAGER');

const p = new URLSearchParams(location.search);
const id = p.get('id');
const msg = document.getElementById('msg');
// quay lai danh sach lan nhap voi cac tham so da co
const back = new URLSearchParams(location.search);
back.delete('id');
document.getElementById('back').href = 'GDImportList6.html?' + back.toString();

(async function () {
  try {
    const r = await api('/api/stat/imports/detail?id=' + encodeURIComponent(id));
    document.getElementById('code').textContent = r.code;
    document.getElementById('date').textContent = fmtDate(r.importDate);
    document.getElementById('sup').textContent = r.supplierName;
    document.getElementById('staff').textContent = r.staffName;
    document.getElementById('note').textContent = r.note || '';
    const body = document.getElementById('lines');
    r.lines.forEach(function (l) {
      const tr = document.createElement('tr');
      tr.append(
        td(String(l.stt)),
        td('PT' + String(l.accessoryId).padStart(3, '0')),
        td(l.accessoryName),
        td(String(l.quantity), 'num'),
        td(fmtMoney(l.unitPrice), 'num'),
        td(fmtMoney(l.amount), 'num')
      );
      body.appendChild(tr);
    });
    document.getElementById('total').textContent = fmtMoney(r.totalAmount);
    document.getElementById('content').classList.remove('hidden');
  } catch (err) {
    showError(msg, err.message);
  }
})();
