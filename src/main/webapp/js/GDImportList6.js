/* GDImportList6: danh sach cac lan nhap cua nha cung cap da chon; click ma lan nhap de xem hoa don */
requireRole('MANAGER');

const p = new URLSearchParams(location.search);
const supplierId = p.get('supplierId');
const start = p.get('start');
const end = p.get('end');
const msg = document.getElementById('msg');
document.getElementById('back').href = 'GDStatSupplier6.html?start=' + start + '&end=' + end;
document.getElementById('range').textContent = fmtDate(start) + ' - ' + fmtDate(end);

(async function () {
  try {
    const q = 'supplierId=' + encodeURIComponent(supplierId) + '&start=' + encodeURIComponent(start) + '&end=' + encodeURIComponent(end);
    const data = await api('/api/stat/imports?' + q);
    document.getElementById('sup').textContent = data.supplier.name;
    if (data.receipts.length === 0) {
      document.getElementById('empty').classList.remove('hidden');
      return;
    }
    const body = document.querySelector('#tbl tbody');
    data.receipts.forEach(function (r, i) {
      const tr = document.createElement('tr');
      tr.append(
        td(String(i + 1)),
        td(r.code, '', 'GDImportDetail6.html?id=' + r.id + '&' + q),
        td(fmtDate(r.importDate)),
        td(r.staffName),
        td(String(r.totalQuantity), 'num')
      );
      body.appendChild(tr);
    });
    document.getElementById('tbl').classList.remove('hidden');
  } catch (err) {
    showError(msg, err.message);
  }
})();
