document.addEventListener('DOMContentLoaded', function() {
    // Assuming the modal is shown based on some condition
    var submitButton = document.getElementById('submitFeedback');
    submitButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the form from submitting normally
        var feedbackData = {
            rating: document.getElementById('rating').value,
            comment: document.getElementById('comment').value,
            // Add other necessary fields here
        };
        submitFeedback(feedbackData);
    });
});

function submitFeedback(feedbackData) {
    fetch('/feedback/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(feedbackData),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            // Close the modal and show a success message or similar
            // Reset or hide the modal fields as needed
        })
        .catch((error) => {
            console.error('Error:', error);
            // Handle errors, such as by showing an error message within the modal
        });
}