document.addEventListener('DOMContentLoaded', function() {
    const filterOverlay = document.getElementById('filterOverlay');
    const filterPanel = document.getElementById('filterPanel');
    const applyFiltersBtn = document.getElementById('applyFiltersBtn');
    const clearFiltersBtn = document.getElementById('clearFiltersBtn');

    if (filterOverlay && filterPanel) {
        window.openFilter = function() {
            filterOverlay.classList.remove('hidden');
            document.body.style.overflow = 'hidden';
            requestAnimationFrame(() => {
                filterPanel.classList.remove('translate-y-full');
                filterPanel.classList.add('w-full');
            });
        };

        window.closeFilter = function() {
            filterPanel.classList.add('translate-y-full');
            document.body.style.overflow = '';
            setTimeout(() => {
                filterOverlay.classList.add('hidden');
                filterPanel.classList.remove('w-full');
            }, 300);
        };

        // Close filter when clicking outside
        filterOverlay.addEventListener('click', function(event) {
            if (event.target === this) {
                window.closeFilter();
            }
        });

        // Prevent closing when clicking inside the panel
        filterPanel.addEventListener('click', function(event) {
            event.stopPropagation();
        });
    }

    function clearFilters() {
        document.querySelectorAll('input[type="checkbox"], input[type="radio"]').forEach(input => {
            input.checked = false;
        });
        updateFilterStyles();
    }

    function applyFilters() {
        const selectedColors = Array.from(document.querySelectorAll('input[name="colors"]:checked'))
            .map(input => input.value);
        const selectedSizes = Array.from(document.querySelectorAll('input[name="sizes"]:checked'))
            .map(input => input.value);
        const selectedPriceRange = Array.from(document.querySelectorAll('input[name="price"]:checked'))
            .map(input => input.value);

        const params = new URLSearchParams(window.location.search);
        if (selectedColors.length) params.set('colors', selectedColors.join(','));
        if (selectedSizes.length) params.set('sizes', selectedSizes.join(','));
        if (selectedPriceRange.length) params.set('prices', selectedPriceRange.join(','));

        window.location.href = `${window.location.pathname}?${params.toString()}`;
    }

    function updateFilterStyles() {
        document.querySelectorAll('input[type="checkbox"], input[type="radio"]').forEach(input => {
            const label = input.nextElementSibling;
            if (label) {
                if (input.checked) {
                    label.classList.add('border-[#31495e]');
                } else {
                    label.classList.remove('border-[#31495e]');
                }
            }
        });

        document.querySelectorAll('input[type="radio"]').forEach(radio => {
            const dot = radio.nextElementSibling?.querySelector('span span');
            if (dot) {
                if (radio.checked) {
                    dot.classList.remove('hidden');
                } else {
                    dot.classList.add('hidden');
                }
            }
        });

        document.querySelectorAll('.filter-btn').forEach(btn => {
            if (btn.classList.contains('selected')) {
                btn.classList.add('border-blue-500');
            } else {
                btn.classList.remove('border-blue-500');
            }
        });

        document.querySelectorAll('.filter-btn[data-type="size"]').forEach(btn => {
            if (btn.classList.contains('selected')) {
                btn.classList.add('bg-gray-200', 'border');
            } else {
                btn.classList.remove('bg-gray-200', 'border');
            }
        });
    }

    // Attach event listeners
    if (applyFiltersBtn) {
        applyFiltersBtn.addEventListener('click', applyFilters);
    }

    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener('click', clearFilters);
    }

    // Style radio buttons and checkboxes
    document.querySelectorAll('input[type="radio"], input[type="checkbox"]').forEach(input => {
        input.addEventListener('change', updateFilterStyles);
    });

    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            this.classList.toggle('selected');
            updateFilterStyles();
        });
    });

    // Initial update of filter styles
    updateFilterStyles();
});