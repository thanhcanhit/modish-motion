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

function showSuggestedItems() {
    // Ẩn tất cả các danh sách sản phẩm
    document.querySelectorAll('.suggested-items').forEach(item => item.classList.add('hidden'));

    // Lấy giá trị của danh mục đã chọn và hiển thị danh sách tương ứng
    const selectedIndex = document.getElementById('category-select').value;
    document.getElementById(`suggestedItems${parseInt(selectedIndex) + 1}`).classList.remove('hidden');
    document.getElementById(`suggestedItems${parseInt(selectedIndex) + 1}`).classList.add('grid');
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

