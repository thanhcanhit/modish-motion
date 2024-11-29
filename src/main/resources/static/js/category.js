let currentPage = 1;
let selectedColors = [];
let selectedSizes = [];
let selectedPriceRange = '';

function initializeFilters() {
    // Get selected filters from URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    selectedColors = urlParams.getAll('colors');
    selectedSizes = urlParams.getAll('sizes');
    selectedPriceRange = urlParams.get('price') || '0-1000000000';
    currentPage = parseInt(urlParams.get('page')) || 1;

    // Update UI to reflect current filters
    updateFilterUI();
}

function updateFilterUI() {
    // Update color buttons
    document.querySelectorAll('[data-type="color"]').forEach(btn => {
        const colorId = btn.getAttribute('data-value');
        if (selectedColors.includes(colorId)) {
            btn.classList.add('selected');
        }
    });

    // Update size buttons
    document.querySelectorAll('[data-type="size"]').forEach(btn => {
        const sizeId = btn.getAttribute('data-value');
        if (selectedSizes.includes(sizeId)) {
            btn.classList.add('selected');
        }
    });

    // Update price radio buttons
    document.querySelectorAll('input[name="priceRange"]').forEach(radio => {
        if (radio.value === selectedPriceRange) {
            radio.checked = true;
        }
    });
}

function toggleFilter(type, value, btn) {
    if (type === 'color') {
        const index = selectedColors.indexOf(value);
        if (index === -1) {
            selectedColors.push(value);
        } else {
            selectedColors.splice(index, 1);
        }
    } else if (type === 'size') {
        const index = selectedSizes.indexOf(value);
        if (index === -1) {
            selectedSizes.push(value);
        } else {
            selectedSizes.splice(index, 1);
        }
    }
    btn.classList.toggle('selected');
}

function applyFilters() {
    currentPage = 1; // Reset to first page when applying new filters
    const url = new URL(window.location.href);
    url.searchParams.delete('colors');
    url.searchParams.delete('sizes');
    url.searchParams.delete('price');
    url.searchParams.delete('page');

    selectedColors.forEach(color => url.searchParams.append('colors', color));
    selectedSizes.forEach(size => url.searchParams.append('sizes', size));
    if (selectedPriceRange) {
        url.searchParams.set('price', selectedPriceRange);
    }
    url.searchParams.set('page', currentPage);

    window.location.href = url.toString();
}

function clearFilters() {
    selectedColors = [];
    selectedSizes = [];
    selectedPriceRange = '0-1000000000';
    currentPage = 1;

    const url = new URL(window.location.href);
    url.search = '';
    window.location.href = url.toString();
}

function loadMoreItems() {
    currentPage++;
    const url = new URL(window.location.href);
    url.searchParams.set('page', currentPage);

    fetch(url.toString())
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newItems = doc.querySelectorAll('#productGrid > div');
            const productGrid = document.getElementById('productGrid');

            newItems.forEach(item => {
                productGrid.appendChild(item.cloneNode(true));
            });

            const hasMore = doc.getElementById('hasMore').value === 'true';
            if (!hasMore) {
                document.getElementById('loadMoreBtn').style.display = 'none';
            }
        });
}

// Initialize filters when page loads
document.addEventListener('DOMContentLoaded', initializeFilters);

