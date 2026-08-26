#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Diagramas VECTORIALES de stack y heap para el apartado 3B del apunte.

Formato decidido por la cátedra (2026-08):
- reportlab.graphics.shapes -> PDF vectorial (nítido a cualquier zoom, ~KB).
  NUNCA JPG/PNG: el texto chico y las líneas finas salen borrosos.
- Colores de pizarrón: NEGRO la estructura, ROJO lo que cambió en este paso,
  VERDE lo recién creado (y la X de los frames desapilados), GRIS lo
  inalcanzable / desapilado.
- Línea vertical divide STACK (izquierda) y HEAP (derecha). Frames apilados
  hacia abajo (main arriba, la llamada más profunda abajo). Objetos como
  cajas a la derecha. Flechas reales cruzando la línea.
- Frame desapilado = caja gris con una X verde encima.
- SIN caracteres unicode de dibujo en textos (│ ┌ ● ▶): ancho ambiguo.
  Las flechas y viñetas se dibujan con vectores.

API declarativa:

    dibujar("e01_p2.pdf",
            frames=[Frame("main", [Var("r1", apunta="@A", estado="nuevo")])],
            objetos=[Obj("@A", [("base", "3")], etiqueta="Rectangulo",
                         estado="nuevo")],
            nota="new reservo el bloque @A en el heap")

Estados admitidos en Frame / Var / Obj / campo: "normal" (negro),
"cambio" (rojo), "nuevo" (verde), "gris" (inalcanzable / desapilado).
En Frame, ademas: desapilado=True dibuja la caja gris con la X verde.
Un campo de un Obj puede apuntar a otro Obj: ("marco", "@B") con
apunta=True en la tupla extendida ("marco", "@B", "normal", True).
"""

from reportlab.graphics.shapes import (Drawing, Group, Line, PolyLine,
                                       Polygon, Rect, String)
from reportlab.graphics import renderPDF
from reportlab.lib.colors import HexColor

NEGRO = HexColor("#1a1a1a")
ROJO = HexColor("#c00000")
VERDE = HexColor("#1a7a1a")
GRIS = HexColor("#9a9a9a")
GRIS_CLARO = HexColor("#c8c8c8")
BLANCO = HexColor("#ffffff")

COLORES = {"normal": NEGRO, "cambio": ROJO, "nuevo": VERDE, "gris": GRIS}

# --- geometría (en puntos) -------------------------------------------------
ANCHO = 470
X_DIV = 225                 # la línea vertical STACK | HEAP
FRAME_X, FRAME_W = 18, 180
OBJ_X, OBJ_W = 285, 160
FILA = 15                   # alto de una fila de variable/campo
CABECERA = 17               # alto de la cabecera de un frame/objeto
SEP_FRAMES = 12
SEP_OBJS = 16
MARGEN_SUP = 30
FUENTE = "Helvetica"
FUENTE_B = "Helvetica-Bold"
FUENTE_M = "Courier"
FUENTE_MB = "Courier-Bold"


class Var:
    def __init__(self, nombre, valor=None, apunta=None, estado="normal"):
        self.nombre = nombre
        self.valor = valor          # texto: "5", "3", "null"...
        self.apunta = apunta        # "@A": dibuja flecha hacia ese objeto
        self.estado = estado


class Frame:
    def __init__(self, nombre, variables=None, estado="normal", desapilado=False):
        self.nombre = nombre
        self.variables = variables or []
        self.estado = estado
        self.desapilado = desapilado


class Obj:
    """campos: lista de tuplas (nombre, valor[, estado[, apunta]]).
    Si apunta=True, `valor` es la dirección de otro Obj y se dibuja flecha."""

    def __init__(self, direccion, campos=None, etiqueta="", estado="normal"):
        self.direccion = direccion
        self.campos = campos or []
        self.etiqueta = etiqueta
        self.estado = estado


def _campo(tupla):
    nombre, valor = tupla[0], tupla[1]
    estado = tupla[2] if len(tupla) > 2 else None
    apunta = tupla[3] if len(tupla) > 3 else False
    return nombre, valor, estado, apunta


def _flecha(d, x1, y1, x2, y2, color, codo=None):
    """flecha con codo opcional (lista de puntos intermedios)."""
    puntos = [x1, y1]
    if codo:
        for (cx, cy) in codo:
            puntos += [cx, cy]
    puntos += [x2, y2]
    d.add(PolyLine(puntos, strokeColor=color, strokeWidth=1.4))
    # punta: triángulo orientado según el último tramo
    px, py = puntos[-4], puntos[-3]
    import math
    ang = math.atan2(y2 - py, x2 - px)
    L, A = 7.0, 3.6
    d.add(Polygon([x2, y2,
                   x2 - L * math.cos(ang) - A * math.sin(ang),
                   y2 - L * math.sin(ang) + A * math.cos(ang),
                   x2 - L * math.cos(ang) + A * math.sin(ang),
                   y2 - L * math.sin(ang) - A * math.cos(ang)],
                  fillColor=color, strokeColor=None))


def _alto_frame(f):
    return CABECERA + FILA * max(1, len(f.variables)) + 4


def _alto_obj(o):
    return CABECERA + FILA * max(1, len(o.campos)) + 4


def dibujar(nombre_archivo, frames=None, objetos=None, nota=None, titulo=None):
    frames = frames or []
    objetos = objetos or []

    alto_stack = MARGEN_SUP + sum(_alto_frame(f) + SEP_FRAMES for f in frames)
    alto_heap = MARGEN_SUP + sum(_alto_obj(o) + SEP_OBJS + 12 for o in objetos)
    alto = max(alto_stack, alto_heap, 90) + (26 if nota else 8) + (18 if titulo else 0)

    d = Drawing(ANCHO, alto)
    y_techo = alto - (18 if titulo else 0)

    if titulo:
        d.add(String(ANCHO / 2, alto - 12, titulo, fontName=FUENTE_B,
                     fontSize=10, fillColor=NEGRO, textAnchor="middle"))

    # cabeceras y línea divisoria
    d.add(String((FRAME_X + FRAME_W / 2) + 10, y_techo - 13, "STACK",
                 fontName=FUENTE_B, fontSize=11, fillColor=NEGRO,
                 textAnchor="middle"))
    d.add(String(OBJ_X + OBJ_W / 2, y_techo - 13, "HEAP",
                 fontName=FUENTE_B, fontSize=11, fillColor=NEGRO,
                 textAnchor="middle"))
    base_y = 24 if nota else 8
    d.add(Line(X_DIV, base_y - 4, X_DIV, y_techo - 4,
               strokeColor=NEGRO, strokeWidth=1.1, strokeDashArray=[4, 3]))

    # --- objetos del heap (primero, para conocer sus posiciones) ----------
    pos_obj = {}
    y = y_techo - MARGEN_SUP
    for o in objetos:
        h = _alto_obj(o)
        col_borde = COLORES.get(o.estado, NEGRO)
        col_texto = GRIS if o.estado == "gris" else NEGRO
        # etiqueta arriba: "@A : Rectangulo"
        etiqueta = o.direccion + (" : " + o.etiqueta if o.etiqueta else "")
        d.add(String(OBJ_X, y + 3, etiqueta, fontName=FUENTE_MB, fontSize=8.5,
                     fillColor=col_borde))
        caja_y = y - h
        d.add(Rect(OBJ_X, caja_y, OBJ_W, h, strokeColor=col_borde,
                   strokeWidth=1.6 if o.estado != "gris" else 1.1,
                   fillColor=BLANCO))
        pos_obj[o.direccion] = (OBJ_X, caja_y, OBJ_W, h)
        fy = y - CABECERA
        campos_pos = {}
        for tupla in o.campos:
            nombre, valor, estado, apunta = _campo(tupla)
            col = COLORES.get(estado) if estado else col_texto
            texto = nombre + " = " + ("" if apunta else str(valor))
            d.add(String(OBJ_X + 8, fy - 3, texto, fontName=FUENTE_M,
                         fontSize = 8.5, fillColor=col))
            campos_pos[nombre] = fy
            if apunta:
                # flecha heap -> heap (por fuera, a la derecha)
                destino = valor
                pos_obj.setdefault("__pend__", []).append(
                    (OBJ_X + 8 + len(texto) * 5.1, fy, destino,
                     col if estado else col_borde))
            fy -= FILA
        if o.estado == "gris":
            d.add(String(OBJ_X + OBJ_W - 6, caja_y + 5, "(inalcanzable)",
                         fontName=FUENTE, fontSize=7, fillColor=GRIS,
                         textAnchor="end"))
        y = caja_y - SEP_OBJS - 12

    # --- frames del stack --------------------------------------------------
    y = y_techo - MARGEN_SUP
    flechas = []                       # (x, y, destino, color)
    for f in frames:
        h = _alto_frame(f)
        caja_y = y - h
        if f.desapilado:
            col_borde, col_texto = GRIS_CLARO, GRIS
        else:
            col_borde = COLORES.get(f.estado, NEGRO)
            col_texto = GRIS if f.estado == "gris" else NEGRO
        d.add(Rect(FRAME_X, caja_y, FRAME_W, h, strokeColor=col_borde,
                   strokeWidth=1.6 if not f.desapilado else 1.0,
                   fillColor=BLANCO))
        # cabecera con el nombre del método
        d.add(Line(FRAME_X, y - CABECERA + 2, FRAME_X + FRAME_W,
                   y - CABECERA + 2, strokeColor=col_borde, strokeWidth=0.8))
        d.add(String(FRAME_X + 7, y - 12, f.nombre + "()",
                     fontName=FUENTE_MB, fontSize=9, fillColor=col_texto))
        fy = y - CABECERA - 3
        for v in f.variables:
            col = GRIS if (f.desapilado or f.estado == "gris") \
                else COLORES.get(v.estado, NEGRO)
            if v.apunta and not f.desapilado:
                texto = v.nombre + " ="
                d.add(String(FRAME_X + 10, fy - 8, texto, fontName=FUENTE_M,
                             fontSize=8.5, fillColor=col))
                # puntito de origen + flecha diferida
                ox = FRAME_X + 10 + (len(texto)) * 5.1 + 5
                oy = fy - 5
                d.add(Rect(ox - 2.2, oy - 2.2, 4.4, 4.4, fillColor=col,
                           strokeColor=None))
                flechas.append((ox, oy, v.apunta, col))
            else:
                valor = v.apunta if v.apunta else v.valor
                texto = v.nombre + " = " + str(valor)
                d.add(String(FRAME_X + 10, fy - 8, texto, fontName=FUENTE_M,
                             fontSize=8.5, fillColor=col))
            fy -= FILA
        if f.desapilado:
            # X verde sobre el frame desapilado
            d.add(Line(FRAME_X + 4, caja_y + 4, FRAME_X + FRAME_W - 4,
                       caja_y + h - 4, strokeColor=VERDE, strokeWidth=2.6))
            d.add(Line(FRAME_X + 4, caja_y + h - 4, FRAME_X + FRAME_W - 4,
                       caja_y + 4, strokeColor=VERDE, strokeWidth=2.6))
        y = caja_y - SEP_FRAMES

    # --- flechas stack -> heap (ruteo ortogonal con carriles) --------------
    # cada flecha usa su propio carril vertical entre la division y el heap,
    # y entra al objeto por una altura propia: nada se superpone.
    llegadas = {}                       # destino -> cuantas flechas ya llegan
    for k, (ox, oy, destino, col) in enumerate(flechas):
        if destino not in pos_obj:
            continue
        (dx, dy, dw, dh) = pos_obj[destino]
        n = llegadas.get(destino, 0)
        llegadas[destino] = n + 1
        ty = dy + dh - CABECERA / 2 - 3 - n * 9        # entrada escalonada
        lane = X_DIV + 12 + k * 9                      # carril propio
        if abs(oy - ty) <= 5:                          # casi recta: directa
            _flecha(d, ox, oy, dx - 2, ty, col)
        else:
            _flecha(d, ox, oy, dx - 2, ty, col,
                    codo=[(lane, oy), (lane, ty)])

    # --- flechas heap -> heap (rodean por la derecha, cada una su carril) --
    for k, pend in enumerate(pos_obj.get("__pend__", [])):
        (ox, oy, destino, col) = pend
        if destino not in pos_obj:
            continue
        (dx, dy, dw, dh) = pos_obj[destino]
        n = llegadas.get(destino, 0)
        llegadas[destino] = n + 1
        ty = dy + dh - CABECERA / 2 - 3 - n * 9
        lane = OBJ_X + OBJ_W + 16 + k * 9
        d.add(Rect(ox + 1, oy - 5.2, 4.4, 4.4, fillColor=col, strokeColor=None))
        _flecha(d, ox + 3, oy - 3, dx + dw + 2, ty, col,
                codo=[(lane, oy - 3), (lane, ty)])

    if nota:
        d.add(String(ANCHO / 2, 8, nota, fontName="Helvetica-Oblique",
                     fontSize=8.5, fillColor=NEGRO, textAnchor="middle"))

    renderPDF.drawToFile(d, nombre_archivo)
    print("OK", nombre_archivo)


if __name__ == "__main__":
    # autoprueba: paso 3 del escenario 4 (swap de punteros adentro del método)
    dibujar("autoprueba.pdf",
            frames=[
                Frame("main", [Var("r1", apunta="@A"), Var("r2", apunta="@B")]),
                Frame("intercambiar", [
                    Var("x", apunta="@B", estado="cambio"),
                    Var("y", apunta="@A", estado="cambio"),
                    Var("tmp", apunta="@A")]),
            ],
            objetos=[
                Obj("@A", [("base", "3")], etiqueta="Rectangulo"),
                Obj("@B", [("base", "7")], etiqueta="Rectangulo"),
            ],
            nota="las copias x e y se cruzaron; r1 y r2 ni se enteraron")
