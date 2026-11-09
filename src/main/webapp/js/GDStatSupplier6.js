/* GDStatSupplier6: bang thong ke nha cung cap theo luong nhap; click ten NCC de xem cac lan nhap */
requireRole('MANAGER');

const p = new URLSearchParams(location.search);
const start = p.get('start');
const end = p.get('end');
const msg = document.getElementById('msg');
document.getElementById('back').href = 'GDSelectDate6.html?start=' + start + '&end=' + end;
document.getElementById('range').textContent = 'Từ ngày ' + fmtDate(start) + ' đến ngày ' + fmtDate(end);

(async function () {
  try {
    const rows = await api('/api/stat/suppliers?start=' + encodeURIComponent(start) + '&end=' + encodeURIComponent(end));
    if (rows.length === 0) {
      document.getElementById('empty').classList.remove('hidden');
      return;
    }
    const body = document.querySelector('#tbl tbody');
    rows.forEach(function (r, i) {
      const tr = document.createElement('tr');
      tr.append(
        td(String(i + 1)),
        td('NCC' + String(r.id).padStart(2, '0')),
        td(r.name, '', 'GDImportList6.html?supplierId=' + r.id + '&start=' + start + '&end=' + end),
        td(r.address || ''),
        td(String(r.totalQuantity), 'num')
      );
      body.appendChild(tr);
    });
    document.getElementById('tbl').classList.remove('hidden');
  } catch (err) {
    showError(msg, err.message);
  }
})();
