// filter.js
let selectedColors = [];
let selectedSizes = [];
let selectedPriceRange = '';

function initializeFilters() {
    // Get initial state from hidden inputs
    const selectedColorsInput = document.getElementById('selectedColors');
    const selectedSizesInput = document.getElementById('selectedSizes');
    const selectedPriceInput = document.getElementById('selectedPrice');

    if (selectedColorsInput && selectedColorsInput.value) {
        selectedColors = selectedColorsInput.value.split(',').map(Number);
    }
    if (selectedSizesInput && selectedSizesInput.value) {
        selectedSizes = selectedSizesInput.value.split(',').map(Number);
    }
    if (selectedPriceInput && selectedPriceInput.value) {
        selectedPriceRange = selectedPriceInput.value;
    }

    // Update UI to reflect current filters
    updateFilterUI();
}

function updateFilterUI() {
    // Update color buttons
    document.querySelectorAll('[data-type="color"]').forEach(btn => {
        const colorId = parseInt(btn.getAttribute('data-value'));
        if (selectedColors.includes(colorId)) {
            btn.classList.add('selected');
        }
    });

    // Update size buttons
    document.querySelectorAll('[data-type="size"]').forEach(btn => {
        const sizeId = parseInt(btn.getAttribute('data-value'));
        if (selectedSizes.includes(sizeId)) {
            btn.classList.add('selected');
        }
    });

    // Update price radio buttons
    if (selectedPriceRange) {
        const radio = document.querySelector(`input[name="priceRange"][value="${selectedPriceRange}"]`);
        if (radio) {
            radio.checked = true;
        }
    }
}

function toggleFilter(type, value, btn) {
    value = parseInt(value);
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
    const url = new URL(window.location.href);
    url.searchParams.delete('colors');
    url.searchParams.delete('sizes');
    url.searchParams.delete('price');
    url.searchParams.set('page', '1');

    if (selectedColors.length > 0) {
        selectedColors.forEach(color => url.searchParams.append('colors', color));
    }
    if (selectedSizes.length > 0) {
        selectedSizes.forEach(size => url.searchParams.append('sizes', size));
    }
    if (selectedPriceRange) {
        url.searchParams.set('price', selectedPriceRange);
    }

    window.location.href = url.toString();
}

function clearFilters() {
    selectedColors = [];
    selectedSizes = [];
    selectedPriceRange = '';

    // Reset UI
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('selected');
    });
    document.querySelectorAll('input[name="priceRange"]').forEach(radio => {
        radio.checked = false;
    });

    // Redirect to base category URL
    const url = new URL(window.location.href);
    url.search = 'page=1';
    window.location.href = url.toString();
}