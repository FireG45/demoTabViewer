
export default function getRole(token) {
    let role;
    fetch('http://localhost:8080/auth/role', {
        headers: new Headers({
            'Authorization': 'Bearer ' + token
        })
    }).then((response) => {
        response.text().then((res) => role = res);
    });

    return role;
}