function selectTrack() {
    const select = document.getElementById("selectTrack");
    console.log(window.location.href)
    window.location.href = '/?track=' + select.options[select.selectedIndex].value
    console.log(window.location.href)
}