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



function updateUrlAndReload(key, value) {
    if ('URLSearchParams' in window) {
        let searchParams = new URLSearchParams(window.location.search);
        searchParams.set(key, value);
        searchParams.set('page', 0); // Reset page to 0 when searching
        window.location.href = window.location.pathname + '?' + searchParams.toString(); // Trigger a page reload with the new URL
    }
}

