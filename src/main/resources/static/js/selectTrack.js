function selectTrack(id) {
    const select = document.getElementById("selectTrack");
    console.log(window.location.href)
    window.location.href = window.location = '/tabs/' + id + '?track=' + select.options[select.selectedIndex].value
}