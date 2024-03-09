{
    const toggleSidebar = document.getElementById('toggleSidebar')

    const sidebar = document.getElementById('sidebar')
    toggleSidebar.addEventListener('click', function (evt) {
        if(sidebar.classList.contains('active')) {
            sidebar.classList.remove('active')
        } else {
            sidebar.classList.add('active')
        }
    })
}