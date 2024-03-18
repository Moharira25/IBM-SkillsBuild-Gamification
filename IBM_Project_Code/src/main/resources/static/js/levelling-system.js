const levelBar = document.getElementById('levelBar');
const maxClicks = 10;
let clickCount = 0;
let currentLevel = 1;
let isAnimating = false;
let hideTimeout;
const segmentValue = localStorage.getItem('segmentValue');
const multiplier = parseFloat(segmentValue.substring(1)); // Extract the multiplier from the segment value
const orbSound = new Audio('orb.mp3');
const levelSound = new Audio('levelup.mp3');

document.addEventListener('keydown', (event) => {
    if (event.code === 'Space' && clickCount < maxClicks && !isAnimating) {
        showLevelBarSmooth();
        setTimeout(incrementLevel, 500);
    }
});

function incrementLevel() {
    const increment = 100 / maxClicks;
    clickCount++;
    const newWidth = increment * clickCount * multiplier; // Multiply the increment by the multiplier
    levelBar.querySelector('.fill-increment').style.width = `${newWidth}%`;
    // orbSound.currentTime = 0;
    // orbSound.play();
    if (clickCount === maxClicks) {
        setTimeout(() => {
            levelUp();
            resetLevelBar();
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