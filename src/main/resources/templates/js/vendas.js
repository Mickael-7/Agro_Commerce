function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function() {
        const output = document.getElementById('product-image-preview');
        output.src = reader.result;
    }
    if (event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0]);
    }
}

document.getElementById('value').addEventListener('input', function (e) {
    const value = e.target.value;
    if (!/^\d*\.?\d*$/.test(value)) {
        e.target.value = value.slice(0, -1);
    }
});

function submitForm() {
    const form = document.getElementById('reservation-form');
    const data = {
        type: form.type.value,
        name: form.name.value,
        description: form.description.value,
        value: form.value.value
    };
     const token = localStorage.getItem("token");

    fetch('http://localhost:8090/products/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
             Authorization:  `Bearer ${token}`,
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            alert('Produto cadastrado com sucesso!');
        } else {
            alert('Falha ao cadastrar produto.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Um erro ocorreu durante o cadastro do produto');
    });
}
