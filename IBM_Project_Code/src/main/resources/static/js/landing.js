function toggleDropdown() {
    let dropdownMenu = document.getElementById("dropdownMenu");
    dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
}

function adjustContentPadding() {
    const header = document.querySelector('nav'); // Adjust this selector based on your actual header
    const mainContent = document.getElementById('main-content');
    if (header && mainContent) {
        const headerHeight = header.offsetHeight;
        mainContent.style.paddingTop = `${headerHeight}px`;
    }
}

window.onload = adjustContentPadding;
window.onresize = adjustContentPadding;