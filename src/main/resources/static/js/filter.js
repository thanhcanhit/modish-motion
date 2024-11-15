function openFilter() {
    const overlay = document.getElementById('filterOverlay');
    const panel = document.getElementById('filterPanel');

    overlay.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
    requestAnimationFrame(() => {
        panel.classList.remove('translate-y-full');
    });
}

function closeFilter() {
    const overlay = document.getElementById('filterOverlay');
    const panel = document.getElementById('filterPanel');

    panel.classList.add('translate-y-full');
    document.body.style.overflow = '';
    setTimeout(() => {
        overlay.classList.add('hidden');
    }, 300);
}

function clearFilters() {
    // Clear all checkboxes and radio buttons
    document.querySelectorAll('input[type="checkbox"], input[type="radio"]').forEach(input => {
        input.checked = false;
    });
}

function applyFilters() {
    // Collect selected filters
    const selectedColors = Array.from(document.querySelectorAll('input[name="colors"]:checked'))
        .map(input => input.value);
    const selectedSizes = Array.from(document.querySelectorAll('input[name="sizes"]:checked'))
        .map(input => input.value);
    const selectedPriceRange = document.querySelector('input[name="price-range"]:checked')?.value;

    // Build query parameters
    const params = new URLSearchParams(window.location.search);
    if (selectedColors.length) params.set('colors', selectedColors.join(','));
    if (selectedSizes.length) params.set('sizes', selectedSizes.join(','));
    if (selectedPriceRange) params.set('priceRange', selectedPriceRange);

    // Redirect with filters
    window.location.href = `${window.location.pathname}?${params.toString()}`;
}

// Style radio buttons
document.querySelectorAll('input[type="radio"]').forEach(radio => {
    radio.addEventListener('change', function() {
        // Hide all dots
        document.querySelectorAll('input[type="radio"] + label span span').forEach(dot => {
            dot.classList.add('hidden');
        });
        // Show dot for checked radio
        if (this.checked) {
            this.nextElementSibling.querySelector('span span').classList.remove('hidden');
        }
    });
});

// Highlight selected filters
document.querySelectorAll('input[type="checkbox"], input[type="radio"]').forEach(input => {
    input.addEventListener('change', function() {
        if (this.checked) {
            this.nextElementSibling.classList.add('border-[#31495e]');
        } else {
            this.nextElementSibling.classList.remove('border-[#31495e]');
        }
    });
});

// Close filter when clicking outside
document.getElementById('filterOverlay').addEventListener('click', function(event) {
    if (event.target === this) {
        closeFilter();
    }
});

// Close filter when clicking outside the panel
document.getElementById('filterPanel').addEventListener('click', function(event) {
    event.stopPropagation();
});