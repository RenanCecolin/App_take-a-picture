Aplicativo Android desenvolvido em Kotlin para capturar imagens pela câmera e enviar para uma API utilizando Retrofit e multipart/form-data.

## Funcionalidades implementadas

- Permissão da câmera
- Captura de imagem
- Exibição da foto na tela
- Conversão de Bitmap para arquivo `.jpg`
- Envio da imagem para API com Retrofit

---

# Endpoint utilizado

```http
POST /api/fridge/1/items/upload-image/
```

A imagem é enviada no campo:

```text
image
```

Compatível com o backend Django:

```python
image_file = request.FILES.get('image')
```

---

# Estrutura principal

## MainActivity.kt
Responsável por:
- abrir câmera
- capturar imagem
- converter arquivo
- enviar imagem para API

## RetrofitClient.kt
Configuração do Retrofit e Base URL.

## ApiService.kt
Interface responsável pela chamada da API.

---

# Base URL

No emulator Android:

```kotlin
http://localhost:8000/
```

Em celular físico, trocar pelo IP da máquina que estiver rodando o backend.

---

# Backend esperado

O app foi desenvolvido para integração com uma API Django responsável por:
- receber imagem
- executar YOLO
- detectar alimentos
- adicionar itens na geladeira
