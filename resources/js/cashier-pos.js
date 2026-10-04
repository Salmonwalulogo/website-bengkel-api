const posPage = document.querySelector('.pos-page');

if (posPage) {
    const products = JSON.parse(posPage.dataset.products || '[]');
    const cart = new Map();
    const formatRupiah = (value) => `Rp ${new Intl.NumberFormat('id-ID', { maximumFractionDigits: 0 }).format(Math.max(0, Number(value) || 0))}`;
    const $ = (selector) => posPage.querySelector(selector);
    const searchInput = $('#product-search');
    const results = $('#product-results');
    const cartTable = $('#cart-table');
    const cartBody = cartTable.querySelector('tbody');
    const cartEmpty = $('#cart-empty');
    const paidInput = $('#paid-amount');
    const payButton = $('#pay-button');
    const escapeHtml = (value) => String(value ?? '').replace(/[&<>"']/g, (character) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' }[character]));

    const renderProducts = () => {
        const keyword = searchInput.value.trim().toLowerCase();
        const matches = products.filter((product) => !keyword || [product.name, product.code, product.barcode].some((value) => String(value || '').toLowerCase().includes(keyword)));
        results.innerHTML = matches.length ? matches.map((product) => `<div class="product-result"><div><strong>${escapeHtml(product.name)}</strong><small>${escapeHtml(product.code)} · ${escapeHtml(product.barcode || 'Tanpa barcode')} · Stok ${product.stock}</small><span class="price">${formatRupiah(product.price)}</span></div><button type="button" class="btn-add" data-add="${product.id}" aria-label="Tambah ${escapeHtml(product.name)}"><i class="fas fa-plus"></i></button></div>`).join('') : '<div class="text-muted small">Produk tidak ditemukan.</div>';
    };

    const calculate = () => {
        const items = [...cart.values()];
        const itemCount = items.reduce((sum, item) => sum + item.quantity, 0);
        const subtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        const discountPercent = Math.min(100, Math.max(0, Number($('#discount-percent').value) || 0));
        const discountNominal = Math.max(0, Number($('#discount-nominal').value) || 0);
        const tax = Math.max(0, Number($('#tax').value) || 0);
        const otherFee = Math.max(0, Number($('#other-fee').value) || 0);
        const discount = (subtotal * discountPercent / 100) + discountNominal;
        const total = Math.max(0, subtotal - discount + tax + otherFee);
        const paid = Math.max(0, Number(paidInput.value) || 0);
        const difference = paid - total;
        $('#summary-items').textContent = itemCount;
        $('#summary-subtotal').textContent = formatRupiah(subtotal);
        $('#grand-total').textContent = formatRupiah(total);
        const status = $('#payment-status');
        status.className = `payment-status ${paid === 0 ? 'neutral' : difference < 0 ? 'insufficient' : 'sufficient'}`;
        status.innerHTML = paid === 0 ? '<i class="fas fa-info-circle"></i><span>Masukkan nominal pembayaran.</span>' : difference < 0 ? `<i class="fas fa-exclamation-triangle"></i><span><b>UANG KURANG</b><br>${formatRupiah(Math.abs(difference))}</span>` : `<i class="fas fa-check-circle"></i><span><b>KEMBALIAN</b><br>${formatRupiah(difference)}</span>`;
        payButton.disabled = itemCount === 0 || difference < 0;
        return { itemCount, total };
    };

    const renderCart = () => {
        const items = [...cart.values()];
        cartEmpty.classList.toggle('d-none', items.length > 0);
        cartTable.classList.toggle('d-none', items.length === 0);
        $('#cart-badge').textContent = `${items.reduce((sum, item) => sum + item.quantity, 0)} item`;
        cartBody.innerHTML = items.map((item) => `<tr><td class="cart-product"><strong>${escapeHtml(item.name)}</strong><small>${escapeHtml(item.code)}</small></td><td>${formatRupiah(item.price)}</td><td class="text-center"><div class="qty-control"><button type="button" data-decrease="${item.id}">−</button><input type="number" min="1" max="${item.stock}" value="${item.quantity}" data-quantity="${item.id}"><button type="button" data-increase="${item.id}">+</button></div></td><td class="text-end fw-bold">${formatRupiah(item.price * item.quantity)}</td><td><button type="button" class="btn-remove" data-remove="${item.id}" aria-label="Hapus ${escapeHtml(item.name)}"><i class="fas fa-trash"></i></button></td></tr>`).join('');
        const inputs = $('#cart-inputs');
        inputs.innerHTML = items.map((item, index) => `<input type="hidden" name="items[${index}][product_id]" value="${item.id}"><input type="hidden" name="items[${index}][quantity]" value="${item.quantity}">`).join('');
        calculate();
    };

    const addProduct = (id) => {
        const product = products.find((item) => item.id === Number(id));
        if (!product) return;
        const current = cart.get(product.id);
        cart.set(product.id, { ...product, quantity: Math.min(product.stock, (current?.quantity || 0) + 1) });
        renderCart();
    };

    results.addEventListener('click', (event) => { const button = event.target.closest('[data-add]'); if (button) addProduct(button.dataset.add); });
    cartBody.addEventListener('click', (event) => {
        const target = event.target.closest('button');
        if (!target) return;
        const id = Number(target.dataset.increase || target.dataset.decrease || target.dataset.remove);
        const item = cart.get(id);
        if (target.dataset.remove) cart.delete(id);
        else if (item) item.quantity = target.dataset.increase ? Math.min(item.stock, item.quantity + 1) : Math.max(1, item.quantity - 1);
        renderCart();
    });
    cartBody.addEventListener('change', (event) => { const id = Number(event.target.dataset.quantity); const item = cart.get(id); if (item) item.quantity = Math.min(item.stock, Math.max(1, Number(event.target.value) || 1)); renderCart(); });
    [searchInput, $('#discount-percent'), $('#discount-nominal'), $('#tax'), $('#other-fee'), paidInput].forEach((element) => element.addEventListener(element === searchInput ? 'input' : 'input', element === searchInput ? renderProducts : calculate));
    document.querySelectorAll('[data-quick-pay]').forEach((button) => button.addEventListener('click', () => { paidInput.value = button.dataset.quickPay; calculate(); }));
    $('#exact-payment').addEventListener('click', () => { paidInput.value = calculate().total; calculate(); });

    const held = JSON.parse(localStorage.getItem('kasir-held-transactions') || '[]');
    const renderHeld = () => { $('#held-panel').classList.toggle('d-none', held.length === 0); $('#held-list').innerHTML = held.map((draft, index) => `<div class="held-item"><span><strong>${escapeHtml(draft.number)}</strong><br>${Number(draft.items) || 0} Item · ${formatRupiah(draft.total)}</span><button type="button" class="btn btn-sm btn-outline-success" data-resume="${index}">Lanjutkan</button></div>`).join(''); };
    $('#hold-transaction').addEventListener('click', () => { const summary = calculate(); if (!summary.itemCount) return; held.push({ number: $('#transaction-number').textContent, items: summary.itemCount, total: summary.total, cart: [...cart.values()] }); localStorage.setItem('kasir-held-transactions', JSON.stringify(held)); cart.clear(); renderCart(); renderHeld(); });
    $('#held-list').addEventListener('click', (event) => { const button = event.target.closest('[data-resume]'); if (!button) return; cart.clear(); held[button.dataset.resume].cart.forEach((item) => cart.set(item.id, item)); held.splice(button.dataset.resume, 1); localStorage.setItem('kasir-held-transactions', JSON.stringify(held)); renderCart(); renderHeld(); });
    $('#cancel-transaction').addEventListener('click', () => { if (confirm('Batalkan transaksi saat ini?')) { cart.clear(); $('#pos-form').reset(); $('#checkout-token').value = crypto.randomUUID(); paidInput.value = 0; renderCart(); } });
    document.addEventListener('keydown', (event) => { if (event.key === 'F2') { event.preventDefault(); searchInput.focus(); } if (event.key === 'F4') { event.preventDefault(); paidInput.focus(); } if (event.key === 'Escape') $('#cancel-transaction').click(); });
    $('#transaction-number').textContent = posPage.dataset.transactionNumber;
    $('#pos-form').addEventListener('submit', () => { payButton.disabled = true; payButton.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>MEMPROSES...'; });
    setInterval(() => { $('#live-clock').textContent = new Intl.DateTimeFormat('id-ID', { dateStyle: 'short', timeStyle: 'medium' }).format(new Date()); }, 1000);
    renderProducts(); renderCart(); renderHeld();
}
