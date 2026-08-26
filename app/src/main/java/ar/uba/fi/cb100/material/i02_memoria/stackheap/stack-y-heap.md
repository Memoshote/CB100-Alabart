# Stack y heap paso a paso — los 11 escenarios

Este paquete acompaña el apartado **3B** del apunte (después de las Unidades
1 a 3). Cada escenario es una clase ejecutable con `main`, pensada para
correrse **con el debugger de IntelliJ**: breakpoint, panel Frames (el stack
dibujado), panel Variables (el frame seleccionado), Step Over (F8), Step
Into (F7) y Step Out (Shift + F8) — el cuestionario del TP1 pregunta esto.

| # | Clase | La moraleja |
|---|-------|-------------|
| 0 | `E00PrimitivosPuros` | primitivos: el valor vive en el frame; asignar = fotocopiar |
| 1 | `E01NewSimple` | `new` = bloque en el heap + flecha en la variable; `.` = seguir la flecha |
| 2 | `E02LlamadasAnidadas` | cada llamada apila un frame; el parámetro copia la FLECHA, no el objeto |
| 3 | `E03PunteroThis` | `this`: el puntero oculto al objeto de la llamada |
| 4 | `E04SwapDePunteros` | cruzar copias de flechas en un método NO afecta al llamador |
| 5 | `E05SwapDeValores` | escribir DENTRO de los bloques sí persiste: el heap es compartido |
| 6 | `E06DosNews` | swap de atributos (cambia bloques) vs swap de punteros (cambia flechas) |
| 7 | `E07VariosNews` | varios punteros reasignados: VARIOS bloques huérfanos distintos |
| 8 | `E08HeapApuntaAlHeap` | los atributos también son flechas: el heap apunta al heap |
| 9 | `E09EqualsVsIgualIgual` | `==` compara flechas, `equals` contenido; ojo con el pool de String |
| 10 | `E10DosNewsAlMismoPuntero` | un puntero reasignado: UN huérfano; el puntero sigue válido (≠ escenario 7) |

Clases de apoyo: `Rectangulo` (atributos públicos A PROPÓSITO, para mirar la
memoria sin ruido; en código real van `private`) y `Cuadro` (escenario 8).

## Los diagramas

`diagramas.py` genera los diagramas VECTORIALES del apunte (PDF por paso,
nítidos a cualquier zoom, ~KB cada uno; nunca JPG/PNG, que pixelan el texto
chico). API declarativa:

```python
from diagramas import dibujar, Frame, Var, Obj

dibujar("e01_p1.pdf",
        frames=[Frame("main", [Var("r1", apunta="@A", estado="nuevo")])],
        objetos=[Obj("@A", [("base", "3"), ("altura", "2")],
                     etiqueta="Rectangulo", estado="nuevo")],
        nota="new reservo el bloque @A y guardo la flecha en r1")
```

Convenciones del pizarrón: negro = estructura; rojo = lo que cambió en el
paso; verde = lo recién creado (y la X de un frame desapilado); gris = lo
inalcanzable. Estados: `"normal" | "cambio" | "nuevo" | "gris"`, y en Frame
además `desapilado=True`. Un campo de `Obj` puede apuntar a otro objeto:
`("marco", "@B", None, True)`.

El generador completo de las figuras del apunte está en
`apunte/diagramas/sh/gen_stackheap.py` (35 diagramas). Requiere
`pip install reportlab`. Ojo en Windows: si el PDF está abierto en Acrobat,
la regeneración falla con PermissionError — cerralo antes.
