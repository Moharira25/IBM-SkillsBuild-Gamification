window.addEventListener('scroll', function() {
    var sections = document.querySelectorAll('section');
    var sidebarLinks = document.querySelectorAll('.sidebar a');

    sections.forEach(function(section) {
        var top = section.offsetTop;
        var height = section.offsetHeight;
        var offset = 250;
        if (scrollY >= top - offset && scrollY < top + height - offset){
            var id = section.getAttribute('id');
            sidebarLinks.forEach(function(link) {
                link.classList.remove('active');
                if (link.getAttribute('href').slice(1) === id) {
                    link.classList.add('active');
                }
            });
        }
    });
});