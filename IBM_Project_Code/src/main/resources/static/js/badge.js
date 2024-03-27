document.addEventListener("DOMContentLoaded", function() {
    var badgeObjects = document.querySelectorAll(".badge-obj");
    badgeObjects.forEach(function(badge) {
        var badgeType = badge.dataset.type; // Assuming the badge type is stored in a data-type attribute
        var badgeColor;
        switch (parseInt(badgeType)) {
            case 0:
                badgeColor = "bronze";
                break;
            case 1:
                badgeColor = "silver";
                break;
            case 2:
                badgeColor = "gold";
                break;
            default:
                badgeColor = "goldenrod"; // Default color if type is not recognized
        }
        badge.style.backgroundColor = badgeColor;
    });
});