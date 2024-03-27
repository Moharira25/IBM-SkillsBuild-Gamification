// function filterTable() {
//     let input, filter, table, tr, td, i, txtValue;
//     input = document.getElementById("table-search");
//     filter = input.value.toUpperCase();
//     table = document.querySelector("table"); // Modify this if your table has a specific ID or class
//     tr = table.getElementsByTagName("tr");
//
//     for (i = 0; i < tr.length; i++) {
//         td = tr[i].getElementsByTagName("td")[0]; // Change the index if you want to filter by other columns
//         if (td) {
//             txtValue = td.textContent || td.innerText;
//             if (txtValue.toUpperCase().indexOf(filter) > -1) {
//                 tr[i].style.display = "";
//             } else {
//                 tr[i].style.display = "none";
//             }
//         }
//     }
//     updateUrlParam('search', input.value); // Update URL parameter
// }
//
// function updateUrlParam(key, value) {
//     if ('URLSearchParams' in window) {
//         let searchParams = new URLSearchParams(window.location.search);
//         searchParams.set(key, value);
//         let newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + searchParams.toString();
//         window.history.pushState({path:newUrl}, '', newUrl);
//     }
// }

function filterTable() {
    let input = document.getElementById("table-search");
    let filter = input.value;

    if (window.XMLHttpRequest) { // Check if AJAX is supported
        let xhr = new XMLHttpRequest();
        let url = `/market/listings?search=${encodeURIComponent(filter)}&page=0`;

        xhr.open("GET", url, true);
        xhr.onload = function () {
            if (this.status === 200) {
                let newContent = new DOMParser().parseFromString(this.responseText, 'text/html'); // Create temporary DOM
                let newTable = newContent.getElementById('listings-table-area'); // Assuming you added an ID

                let existingTableArea = document.getElementById('listings-table-area');
                if (existingTableArea) {
                    existingTableArea.parentNode.replaceChild(newTable, existingTableArea); // Replace directly
                } else {
                    // Handle case where table area isn't found (e.g., first load)
                    document.getElementById("listings-container").innerHTML = this.responseText;
                }
                history.pushState(null, '', url);
            }
        };
        xhr.send();
    }
}

const urlParams = new URLSearchParams(window.location.search);
const itemName = urlParams.get('itemName');

document.addEventListener('DOMContentLoaded', function () {
    const modalBuyToggle = document.querySelector('[data-modal-toggle="buyModal"]');
    const modalSellToggle = document.querySelector('[data-modal-toggle="sellModal"]');
    const buyModal = document.getElementById('buyModal');
    const sellModal = document.getElementById('sellModal');
    const priceInput = document.getElementById('price');
    const quantityInput = document.getElementById('quantity');
    const totalCostSpan = document.getElementById('totalCost');
    const placeOrderButton = document.getElementById('placeOrderButton');
    const itemNameSpan = document.getElementById('itemName');
    const itemIdInput = document.getElementById('itemId');


    // Function to open the modal and set item details
    function openBuyModal() {
        buyModal.classList.remove('hidden');
        buyModal.setAttribute('aria-hidden', 'false');
    }

    // Function to close the modal
    function closeBuyModal() {
        buyModal.classList.add('hidden');
        buyModal.setAttribute('aria-hidden', 'true');
    }

    function openSellModal() {
        sellModal.classList.remove('hidden');
        sellModal.setAttribute('aria-hidden', 'false');
    }

    // Function to close the modal
    function closeSellModal() {
        sellModal.classList.add('hidden');
        sellModal.setAttribute('aria-hidden', 'true');
    }

    // Event listeners
    modalBuyToggle.addEventListener('click', closeBuyModal);
    modalSellToggle.addEventListener('click', closeSellModal);
    priceInput.addEventListener('input', updateTotalCost);
    quantityInput.addEventListener('input', updateTotalCost);

    // Update total cost
    function updateTotalCost() {
        const price = parseFloat(priceInput.value) || 0;
        const quantity = parseInt(quantityInput.value, 10) || 0;
        const totalCost = price * quantity;
        totalCostSpan.textContent = totalCost.toFixed(2);
    }

    openBuyModal()
});

document.addEventListener('DOMContentLoaded', function () {
    // Open modal buttons
    const buyButton = document.querySelector('#buyButton');
    const sellButton = document.querySelector('#sellButton'); // Adjust if you have a specific ID or class for the sell button

    // Close modal buttons
    const modalBuyClose = document.querySelector('[data-modal-toggle="buyModal"]');
    const modalSellClose = document.querySelector('[data-modal-toggle="sellModal"]');

    const buyModal = document.getElementById('buyModal');
    const sellModal = document.getElementById('sellModal');

    // Function to toggle the modal's visibility
    function toggleModal(modal) {
        if (modal.classList.contains('hidden')) {
            modal.classList.remove('hidden');
            modal.setAttribute('aria-hidden', 'false');
        } else {
            modal.classList.add('hidden');
            modal.setAttribute('aria-hidden', 'true');
        }
    }

    // Open the Buy Modal
    if (buyButton) {
        buyButton.addEventListener('click', function () {
            toggleModal(buyModal);
        });
    }

    // Open the Sell Modal
    if (sellButton) {
        sellButton.addEventListener('click', function () {
            toggleModal(sellModal);
        });
    }

    // Close the Buy Modal
    if (modalBuyClose) {
        modalBuyClose.addEventListener('click', function () {
            toggleModal(buyModal);
        });
    }

    // Close the Sell Modal
    if (modalSellClose) {
        modalSellClose.addEventListener('click', function () {
            toggleModal(sellModal);
        });
    }

    // Optional: Listen for clicks outside of modals to close them
    window.addEventListener('click', function (event) {
        if (event.target === buyModal) {
            toggleModal(buyModal);
        } else if (event.target === sellModal) {
            toggleModal(sellModal);
        }
    });

    // Assuming updateTotalCost() is defined elsewhere in your script
});

