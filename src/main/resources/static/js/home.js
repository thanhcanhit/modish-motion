// JavaScript for handling banner slider with dot indicators and navigation buttons
const slides = document.getElementsByClassName("customSlider");
const dots = document.getElementsByClassName("dot");
let currentIndex = 0;

// Function to show the current slide based on index
function showSlide(index) {

    // Hide all slides and reset styles for all dots
    for (let i = 0; i < slides.length; i++) {
        slides[i].classList.add("hidden");
        dots[i].classList.remove("bg-opacity", "scale-150");
        dots[i].classList.add("bg-opacity", "scale-100");
    }
    // Show the selected slide and highlight the corresponding dot
    slides[index].classList.remove("hidden");
    dots[index].classList.add("bg-opacity", "scale-150");
}

// Function to go to the next slide
function nextSlide() {
    currentIndex = (currentIndex + 1) % slides.length;
    showSlide(currentIndex);
}

// Function to go to the previous slide
function prevSlide() {
    currentIndex = (currentIndex - 1 + slides.length) % slides.length;
    showSlide(currentIndex);
}

// Event listeners for the next and previous buttons
document.getElementById("next").addEventListener("click", nextSlide);
document.getElementById("prev").addEventListener("click", prevSlide);

// Event listeners for the dots
for (let i = 0; i < dots.length; i++) {
    dots[i].addEventListener("click", function() {
        currentIndex = parseInt(this.getAttribute("data-slide"));
        showSlide(currentIndex);
    });
}
setInterval(nextSlide, 4000);
showSlide(currentIndex);
let selectedCategoryIndex = null; // Lưu chỉ mục của danh mục đã chọn

function selectCategory(element) {
    // Lấy giá trị của danh mục từ thuộc tính data-value
    selectedCategoryIndex = element.getAttribute('data-value');

    // Cập nhật giao diện danh mục được chọn (tùy chỉnh nếu cần)
    const dropdownSummary = document.querySelector('.dropdown summary');
    dropdownSummary.textContent = element.textContent;

    // Gọi hàm hiển thị danh sách sản phẩm gợi ý
    showSuggestedItems();
}
function showSuggestedItems() {
    // Ẩn tất cả các danh sách sản phẩm
    document.querySelectorAll('.suggested-items').forEach(item => item.classList.add('hidden'));

    // Hiển thị danh sách tương ứng với danh mục đã chọn
    if (selectedCategoryIndex !== null) {
        const targetElement = document.getElementById(`suggestedItems${parseInt(selectedCategoryIndex) + 1}`);
        if (targetElement) {
            targetElement.classList.remove('hidden');
            targetElement.classList.add('grid');
        } else {
            console.error(`No matching items found for category index: ${selectedCategoryIndex}`);
        }
    }
}
function showPopularItems(button) {
    document.querySelectorAll('#category-buttons .btn').forEach(btn => {
        btn.classList.remove('btn-warning'); // Xóa màu hiện tại
        btn.classList.add('bg-gray-100'); // Đổi lại màu mặc định
    });

    // Thêm trạng thái active cho nút được chọn
    button.classList.remove('bg-gray-100');
    button.classList.add('btn-warning');
    // Ẩn tất cả các danh sách sản phẩm
    document.querySelectorAll('.popular-items').forEach(item => item.classList.add('hidden'));

    // Lấy chỉ số của danh mục đã chọn và hiển thị danh sách tương ứng
    const selectedIndex = button.getAttribute('data-category-index');
    document.getElementById(`popularItems${parseInt(selectedIndex) + 1}`).classList.remove('hidden');
    document.getElementById(`popularItems${parseInt(selectedIndex) + 1}`).classList.add('grid');
}
function redirectToCategory(section) {
    let categoryIndex = null;

    if (section === 'popular') {
        // Lấy danh mục đã chọn ở Popular Items
        const selectedButton = document.querySelector('#category-buttons .btn-warning');
        if (selectedButton) {
            categoryIndex = selectedButton.getAttribute('data-category-index');
        }
    } else if (section === 'suggested') {
        // Lấy danh mục đã chọn ở Suggested Items
        if (selectedCategoryIndex !== null) {
            categoryIndex = selectedCategoryIndex;
        }
    }

    if (categoryIndex !== null) {
        // Lấy tên danh mục từ danh sách
        const categoryNameElement = (section === 'popular')
            ? document.querySelector(`#category-buttons .btn-warning`)
            : document.querySelector(`.dropdown-content a[data-value="${categoryIndex}"]`);

        const categoryName = categoryNameElement ? categoryNameElement.textContent.trim() : null;

        if (categoryName) {
            // Chuyển hướng đến trang category
            window.location.href = `/categories/${encodeURIComponent(categoryName)}?page=1`;
        } else {
            console.error('Không thể lấy tên danh mục.');
        }
    } else {
        console.error('Không thể xác định danh mục đã chọn.');
    }
}
document.addEventListener("DOMContentLoaded", () => {
    // Lấy danh mục đầu tiên
    const firstCategory = document.querySelector('.dropdown-content a[data-value="0"]');

    if (firstCategory) {
        // Cập nhật danh mục đã chọn
        selectCategory(firstCategory);
    }
});


