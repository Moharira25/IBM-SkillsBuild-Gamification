function filterTable() {
    let input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("table-search");
    filter = input.value.toUpperCase();
    table = document.querySelector("table"); // Modify this if your table has a specific ID or class
    tr = table.getElementsByTagName("tr");

    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0]; // Change the index if you want to filter by other columns
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}