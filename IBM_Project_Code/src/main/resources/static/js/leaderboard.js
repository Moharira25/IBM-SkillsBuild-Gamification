document.addEventListener('DOMContentLoaded', function() {
    // Select all rows within the tbody of the table
    const rows = document.querySelectorAll('.leaderboard-table tr');

    // Iterate through each row
    rows.forEach((row, index) => {
        // Add click event listener to each row
        row.addEventListener('click', function() {
            console.log(`Row ${index + 1} clicked.`); // Example action: log to console
            // redirect to the user profile page (doesn't exist yet, but will be created soon)
            window.location.href = "/user/" + row.getAttribute('data-user-id');
        });

        // Optionally, add hover effect with JavaScript instead of CSS
        row.addEventListener('mouseenter', () => row.classList.add('lead-tbody-tr-hover'));
        row.addEventListener('mouseleave', () => row.classList.remove('lead-tbody-tr-hover'));
    });
});
