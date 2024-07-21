function cadastrar() {
    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const sexo = document.getElementById('sexo').value;
    const birthday = document.getElementById('dataNascimento').value;
    const confirmSenha = document.getElementById('confirmSenha').value;

    if (senha !== confirmSenha) {
        document.getElementById('msgError').innerText = 'As senhas não coincidem.';
        document.getElementById('msgError').style.display = 'block';
        document.getElementById('msgSuccess').style.display = 'none';
        return;
    }

    const data = {
        userName: nome,
        email: email,
        password: senha,
        sex: sexo,
        birthDate: birthday
    };

    fetch('http://localhost:8090/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao cadastrar usuário.');
        }
        return response.text();
    })
    .then(data => {
        document.getElementById('msgSuccess').innerText = data;
        document.getElementById('msgSuccess').style.display = 'block';
        document.getElementById('msgError').style.display = 'none';
        limparCampos();

        window.location.href = '../login/login.html';
    })
    .catch(error => {
        console.error('Erro:', error);
        document.getElementById('msgError').innerText = 'Erro ao cadastrar usuário.';
        document.getElementById('msgError').style.display = 'block';
        document.getElementById('msgSuccess').style.display = 'none';
    });
}

function limparCampos() {
    document.getElementById('nome').value = '';
    document.getElementById('senha').value = '';
    document.getElementById('confirmSenha').value = '';
    document.getElementById('sexo').value = '';
    document.getElementById('dataNascimento').value = '';
    document.getElementById('email').value = '';
}

document.getElementById('verSenha').addEventListener('click', () => {
    const senhaInput = document.getElementById('senha');
    senhaInput.type = senhaInput.type === 'password' ? 'text' : 'password';
});

document.getElementById('verConfirmSenha').addEventListener('click', () => {
    const confirmSenhaInput = document.getElementById('confirmSenha');
    confirmSenhaInput.type = confirmSenhaInput.type === 'password' ? 'text' : 'password';
});
