const levelBar = document.getElementById('levelBar');
const maxClicks = 10;
let clickCount = 0;
let currentLevel = 1;
let isAnimating = false;
let hideTimeout;
const segmentValue = localStorage.getItem('segmentValue');
let multiplier = segmentValue ? parseFloat(segmentValue.substring(1)) : 1; // Default multiplier to 1 if segmentValue is null or undefined
const orbSound = new Audio('orb.mp3');
const levelSound = new Audio('levelup.mp3');

// Retrieve level progress from localStorage if available
const savedLevelProgress = localStorage.getItem('levelProgress');
if (savedLevelProgress) {
    const savedProgress = parseFloat(savedLevelProgress);
    levelBar.querySelector('.fill-increment').style.width = `${savedProgress}%`;
    clickCount = Math.floor(savedProgress / (100 / maxClicks));
}

function incrementLevel() {
    const baseIncrement = 100 / maxClicks; // Base increment
    const increment = baseIncrement * multiplier; // Adjusted increment with multiplier
    const previousWidth = parseFloat(levelBar.querySelector('.fill-increment').style.width) || 0; // Get previous width
    const newWidth = previousWidth + increment; // Calculate new width by adding the increment
    clickCount++;
    levelBar.querySelector('.fill-increment').style.width = `${newWidth}%`;
    // Store progress in localStorage
    localStorage.setItem('levelProgress', `${newWidth}%`);
    // Check if the progress bar is full (reached 100% width)
    if (newWidth >= 100) {
        setTimeout(() => {
            levelUp();
            resetLevelBar();
            localStorage.removeItem('levelProgress'); // Clear progress after level up
            clickCount = 0; // Reset click count
            multiplier = segmentValue ? parseFloat(segmentValue.substring(1)) : 1; // Reset multiplier to default after level up
        }, 1000);
    } else {
        clearTimeout(hideTimeout);
        hideTimeout = setTimeout(hideLevelBar, 5000);
    }
}
function levelUp() {
    currentLevel++;
    showPopUp(`Level ${currentLevel}`);
    // levelSound.currentTime = 0;
    // levelSound.play();
}
function resetLevelBar() {
    clickCount = 0;
    levelBar.querySelector('.fill-increment').style.width = '0';
}

function showLevelBarSmooth() {
    isAnimating = true;
    levelBar.style.transition = 'transform 0.5s ease, opacity 0.5s ease';
    levelBar.style.transform = 'translateY(0)';
    levelBar.style.opacity = '1';
    setTimeout(() => {
        isAnimating = false;
        levelBar.style.transition = '';
    }, 500);
}

function hideLevelBar() {
    levelBar.style.transition = 'transform 0.5s ease, opacity 0.5s ease';
    levelBar.style.transform = 'translateY(-100%)';
    levelBar.style.opacity = '0';
    setTimeout(() => {
        levelBar.style.transition = '';
    }, 500);
}

function showPopUp(message) {
    const popUp = document.createElement('div');
    popUp.classList.add('pop-up');
    popUp.textContent = message;
    document.body.appendChild(popUp);
    setTimeout(() => {
        popUp.remove();
    }, 3000);
}