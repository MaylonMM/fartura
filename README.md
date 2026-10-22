# Fartura

Mod de Minecraft que adiciona comidas típicas brasileiras e de outros lugares, com foco em agricultura.

| Item | Valor |
|---|---|
| Mod ID | `fartura` |
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.233 |
| Java | 21 |

## Estrutura do projeto

```
fartura/
├── settings.gradle.kts      # Declara o Stonecutter e a lista de versões (nós)
├── stonecutter.gradle.kts   # Controller: define a versão ativa no IDE/build
├── build.gradle.kts         # Build compartilhado por todas as versões (NeoGradle)
├── gradle.properties        # Metadados do mod e configurações comuns
├── versions/
│   └── 1.21.1-neoforge/
│       └── gradle.properties  # MC, NeoForge, Parchment e faixas de versão deste nó
├── gradlew / gradlew.bat    # Gradle Wrapper (usar sempre este)
├── gradle/wrapper/
├── src/main/
│   ├── java/com/fartura/
│   │   ├── Fartura.java            # Classe principal do mod (@Mod)
│   │   └── registry/               # Registros: itens, blocos, abas criativas
│   └── resources/
│       ├── META-INF/neoforge.mods.toml   # Metadados do mod (preenchidos via gradle.properties)
│       └── assets/fartura/
│           ├── lang/               # Traduções (pt_br.json, en_us.json)
│           ├── models/item/        # Modelos dos itens
│           └── textures/item/      # Texturas PNG dos itens
├── .github/workflows/build.yml     # CI: compila o mod a cada push
└── .gitignore
```

## Abrindo no IntelliJ IDEA

1. `File > Open` e selecione a pasta do projeto (o IntelliJ detecta o Gradle automaticamente).
2. Use JDK 21 no projeto.
3. Aguarde o primeiro sync do Gradle (baixa o Minecraft e o NeoForge; demora alguns minutos).
4. Use o seletor de versão do Stonecutter no IDE (ou `stonecutter.gradle.kts`) para escolher a versão ativa.
5. Rode `runClient` pela aba Gradle (`fartura > Tasks > neoforge > runClient`) ou pelas configurações de run geradas.

## Comandos

```powershell
.\gradlew.bat build                        # compila a versão ativa e gera o .jar em versions/<nó>/build/libs
.\gradlew.bat :1.21.1-neoforge:runClient   # abre o Minecraft com o mod nesta versão
```

## Compatibilidade com outros mods

Os itens do Fartura entram nas tags comuns do NeoForge (`c:`), como `c:crops/corn`, `c:seeds/corn` e `c:grains/corn`.
Assim, receitas de outros mods e as nossas que usam essas tags aceitam o milho de qualquer mod instalado (Croptopia, Farm and Charm, Farmer's Delight etc.), sem dependência obrigatória.
Para adicionar um item a uma tag, edite `src/main/resources/data/c/tags/item/`.

## Adicionando um novo item de comida

1. Registre em `registry/ModItems.java` com `ITEMS.registerSimpleItem(...)`.
2. Adicione o nome em `lang/pt_br.json` e `lang/en_us.json`.
3. Crie `models/item/<nome>.json` e a textura em `textures/item/<nome>.png`.

## Adicionando uma nova versão (Minecraft ou loader)

1. Em `settings.gradle.kts`, adicione o nó dentro de `stonecutter { create(rootProject) { ... } }`:
   ```kotlin
   versions("1.21.1-neoforge" to "1.21.1", "1.20.1-forge" to "1.20.1")
   ```
2. Crie `versions/1.20.1-forge/gradle.properties` com as propriedades da versão (copie de `1.21.1-neoforge` e ajuste).
3. Use `//? if <condição> { ... } //?}` nos arquivos de `src/` apenas onde a API mudar.
4. Rode `.\gradlew.bat build` para validar a versão ativa.

Com Stonecutter, o código fica em `src/` e cada nó gera a sua variante, sem duplicar o projeto.

## Licença

All Rights Reserved (altere em `gradle.properties` se quiser publicar com outra licença).
