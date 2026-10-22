# Brasil Foods

Mod de Minecraft que adiciona comidas típicas brasileiras.

| Item | Valor |
|---|---|
| Mod ID | `brasilfoods` |
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1.233 |
| Java | 21 |

## Estrutura do projeto

```
brasilfoods/
├── gradle.properties        # Versões (MC, loader, mod) — o único lugar que muda por versão
├── build.gradle             # Configuração de build (NeoGradle)
├── settings.gradle
├── gradlew / gradlew.bat    # Gradle Wrapper (usar sempre este, não o gradle instalado)
├── gradle/wrapper/
├── src/main/
│   ├── java/com/brasilfoods/
│   │   ├── BrasilFoods.java        # Classe principal do mod (@Mod)
│   │   └── registry/               # Registros: itens, blocos, abas criativas
│   └── resources/
│       ├── META-INF/neoforge.mods.toml   # Metadados do mod (preenchidos via gradle.properties)
│       └── assets/brasilfoods/
│           ├── lang/               # Traduções (pt_br.json, en_us.json)
│           ├── models/item/        # Modelos dos itens
│           └── textures/item/      # Texturas PNG dos itens
├── .github/workflows/build.yml     # CI: compila o mod a cada push
└── .gitignore
```

## Abrindo no IntelliJ IDEA

1. `File > Open` e selecione a pasta `brasilfoods` (o IntelliJ detecta o Gradle automaticamente).
2. Use JDK 21 no projeto.
3. Aguarde o primeiro sync do Gradle (baixa o Minecraft e o NeoForge; demora alguns minutos).
4. Rode `runClient` pela aba Gradle (`brasilfoods > Tasks > neoforge > runClient`) ou pelas configurações de run geradas.

## Comandos

```powershell
.\gradlew.bat build        # compila e gera o .jar em build/libs
.\gradlew.bat runClient    # abre o Minecraft com o mod
```

## Adicionando um novo item de comida

1. Registre em `registry/ModItems.java` com `ITEMS.registerSimpleItem(...)`.
2. Adicione o nome em `lang/pt_br.json` e `lang/en_us.json`.
3. Crie `models/item/<nome>.json` e a textura em `textures/item/<nome>.png`.

## Versionamento para outros loaders e versões

Hoje o projeto suporta **uma** combinação (NeoForge 1.21.1). Para expandir (Forge, Fabric, outras versões de Minecraft), a recomendação é migrar para o **[Stonecutter](https://stonecutter.kikugie.dev/)**, que mantém um único código-fonte e gera uma variante por versão.

Estrutura-alvo, quando for necessário:

```
brasilfoods/
├── versions/
│   ├── 1.21.1-neoforge/gradle.properties   # valores do gradle.properties atual
│   ├── 1.21.x-neoforge/gradle.properties
│   └── 1.20.1-forge/gradle.properties
└── src/...                                  # código único, com trechos condicionais por versão
```

Até lá, mantenha `gradle.properties` como fonte das versões e evite espalhar números de versão pelo código.

## Licença

All Rights Reserved (altere em `gradle.properties` se quiser publicar com outra licença).
