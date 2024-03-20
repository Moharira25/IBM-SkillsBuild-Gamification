const SPIN_INTERVAL_MS = 10000;
const LAST_SPIN_KEY = 'lastSpin';

let wheel = document.querySelector('.Wheel');
let spinButton = document.getElementById('spinbutton');
let overlay = document.getElementById('overlay');
let modal = document.getElementById('modal');
let modalContent = document.getElementById('modal-content');
let isSpinning = false;
let stoppedSegment = null; // Variable to store the segment on which the wheel stops

function calculateSectionIndex(rotation) {
    const degreesPerSection = 360 / 8; // Adjust if your wheel has a different number of sections
    let adjustedRotation = (rotation % 360 + 360) % 360; // Normalize rotation
    let rawIndex = Math.floor((360 - adjustedRotation + degreesPerSection / 2) / degreesPerSection);
    return rawIndex % 8; // Adjust based on the number of sections
}

spinButton.onclick = function () {
    if (!canSpinWheel() || isSpinning) {
        alert('You can only spin the wheel once every 24 hours!');
        return;
    }

    isSpinning = true;
    lockButton();

    let randomRotation = Math.floor(Math.random() * 360);
    let finalRotation = 1800 + randomRotation; // Ensures a minimum of 5 rotations for a spin effect

    wheel.style.transition = 'transform 5s ease-out';
    wheel.style.transform = `rotate(${finalRotation}deg)`;

    setTimeout(() => {
        let finalDegrees = finalRotation % 360;
        let sectionIndex = calculateSectionIndex(finalDegrees);
        stoppedSegment = sectionIndex + 1; // Segment numbers start from 1
        let sectionSpan = document.querySelectorAll('.number span')[sectionIndex];
        let sectionContent = sectionSpan.textContent.trim();

        displayModal(sectionContent);
        localStorage.setItem(LAST_SPIN_KEY, new Date().getTime().toString());
        startTimer();
        isSpinning = false;
        wheel.style.transition = 'none'; // Reset the transition to prevent spinning effect on reset
        wheel.style.transform = 'rotate(0deg)'; // Reset the wheel for the next spin
    }, 5000);
};

function startTimer() {
    var remainingTime = 10;
    var button = document.getElementById('spinbutton');
    button.disabled = true;
    button.innerText = "Please wait " + remainingTime + " seconds";
    button.style.fontSize = "7px";
    button.style.textAlign = "center";

    var intervalId = setInterval(function () {
        remainingTime--;
        if (remainingTime <= 0) {
            button.disabled = false;
            button.innerText = "Spin";
            button.style.fontSize = "";
            button.style.textAlign = "";
            clearInterval(intervalId);
        } else {
            button.innerText = "Please wait " + remainingTime + " seconds";
        }
    }, 1000);
}

function goToHome() {
    window.location.href = "home.html"; // Navigate to the Daily Spin page
}

function canSpinWheel() {
    const lastSpinTime = localStorage.getItem(LAST_SPIN_KEY);
    if (lastSpinTime) {
        const currentTime = new Date().getTime();
        const timeSinceLastSpin = currentTime - parseInt(lastSpinTime);
        return timeSinceLastSpin >= SPIN_INTERVAL_MS;
    } else {
        return true;
    }
}

function lockButton() {
    var button = document.getElementById('spinbutton');
    button.disabled = true;
    setTimeout(function () {
        button.disabled = false;
    }, 6000);
}

function shouldShowModal() {
    const lastSpinTime = localStorage.getItem(LAST_SPIN_KEY);
    if (lastSpinTime) {
        const currentTime = new Date().getTime();
        const timeSinceLastSpin = currentTime - parseInt(lastSpinTime);
        return timeSinceLastSpin >= SPIN_INTERVAL_MS;
    } else {
        return true; // Show modal if last spin time is not available
    }
}

function showSpinModal() {
    if (shouldShowModal()) {
        displayModal();
        localStorage.setItem(LAST_SPIN_KEY, new Date().getTime().toString());
    }
    showSpinModal()
}


function displayModal(sectionContent) {
    modalContent.innerHTML = 'Congratulations, you have won ' + sectionContent + ' on your next course!';
    overlay.style.display = 'block';
    modal.style.display = 'block';
    localStorage.setItem('segmentValue', sectionContent);
}


function closeModal() {
    overlay.style.display = 'none';
    modal.style.display = 'none';
}
