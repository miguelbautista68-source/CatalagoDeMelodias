package org.uabc.ic.Control;

import org.uabc.ic.Vista.CatalogoView;
import org.uabc.ic.modelo.CatalogoMelodias;
import org.uabc.ic.modelo.Melodia;

import java.util.ArrayList;

public class ControlCatalogo {
    private CatalogoMelodias modelo;
    private CatalogoView vista;

    public ControlCatalogo(CatalogoView vista, CatalogoMelodias modelo) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion = 0;
        while (opcion != 17) {
            opcion = vista.mostrarMenu();
            switch (opcion) {
                case 1:
                    vista.mostrarCadena("\nRegistrar una nueva melodía");
                    String nombre = vista.leerNombreMelodia();
                    String cantante = vista.leerCantante();
                    String compositor = vista.leerCompositor();
                    String genero = vista.leerGenero();
                    String formato = vista.mostrarObtenerFormato();
                    int anio = vista.leerAnioMelodia();
                    double precio = vista.leerPrecioMelodia();

                    Melodia nueva = new Melodia(nombre, compositor, anio, genero, cantante, precio, formato);
                    boolean yaExistia = modelo.registrarMelodia(nueva); // true = ya existía

                    if (!yaExistia) {
                        vista.mostrarCadena("Melodía registrada exitosamente con ID: " + nueva.getId());
                    } else {
                        vista.mostrarCadena("Error: Ya hay una canción con el mismo cantante y año.");
                    }
                    break;

                case 2:
                    vista.mostrarCadena("\n Catálogo Completo ");
                    vista.mostrarCatalogo(modelo.mostrarTodasMelodias());
                    break;

                case 3:
                    String busqueda = vista.leerNombreMelodia();
                    ArrayList<Melodia> encontradas = modelo.buscar(busqueda);
                    if (encontradas.isEmpty()) {
                        vista.mostrarCadena("No se encontraron melodías con ese nombre.");
                    } else {
                        encontradas.forEach(m -> vista.mostrarCadena(m.toString()));
                    }
                    break;

                case 4:
                    int idEliminar = vista.leerId();
                    if (modelo.removerMelodia(idEliminar)) {
                        vista.mostrarCadena("Melodía eliminada con éxito.");
                    } else {
                        vista.mostrarCadena("No se encontró ninguna melodía con ese ID.");
                    }
                    break;

                case 5:
                    vista.mostrarCadena("\nCorregir / Actualizar Melodía");
                    int idAct = vista.leerId();
                    Melodia existente = modelo.buscar(idAct);

                    if (existente == null) {
                        vista.mostrarCadena("Error: No se encontró ninguna melodía con el ID " + idAct + " (o el catálogo está vacío).");
                    } else {
                        vista.mostrarCadena("Melodía encontrada: " + existente);
                        vista.mostrarCadena("Ingrese los nuevos datos para corregir:");

                        String nuevoNombre = vista.leerNombreMelodia();
                        String nuevoCantante = vista.leerCantante();
                        String nuevoCompositor = vista.leerCompositor();
                        String nuevoGenero = vista.leerGenero();
                        String nuevoFormato = vista.mostrarObtenerFormato();
                        int nuevoAnio = vista.leerAnioMelodia();
                        double nuevoPrecio = vista.leerPrecioMelodia();

                        boolean actualizado = modelo.actualizarMelodia(
                                idAct, nuevoNombre, nuevoCompositor, nuevoAnio,
                                nuevoGenero, nuevoCantante, nuevoPrecio, nuevoFormato);

                        if (actualizado) {
                            vista.mostrarCadena("¡Datos actualizados con éxito!");
                        } else {
                            vista.mostrarCadena("No se pudo actualizar (ya existe otra melodía con ese cantante y año).");
                        }
                    }
                    break;

                case 6:
                    vista.mostrarCadena(String.format("El valor monetario total de la colección es: $%.2f",
                            modelo.obtenerPrecioColeccion()));
                    break;

                case 7:
                    vista.mostrarCadena("Géneros en la colección: " + modelo.obtenerTodosLosGeneros());
                    break;

                case 8:
                    int anioBuscar = vista.leerAnioMelodia();
                    ArrayList<Melodia> porAnio = modelo.melodiasPorAnio(anioBuscar);
                    if (porAnio.isEmpty()) {
                        vista.mostrarCadena("No hay canciones registradas de ese año.");
                    } else {
                        porAnio.forEach(m -> vista.mostrarCadena(m.toString()));
                    }
                    break;

                case 9:
                    String cantanteBusq = vista.leerCantante();
                    ArrayList<Melodia> porCantante = modelo.melodiasPorCantante(cantanteBusq);
                    if (porCantante.isEmpty()) {
                        vista.mostrarCadena("No hay canciones registradas de ese cantante.");
                    } else {
                        porCantante.forEach(m -> vista.mostrarCadena(m.toString()));
                    }
                    break;

                case 10:
                    String compBusq = vista.leerCompositor();
                    ArrayList<String> porComp = modelo.melodiasPorCompositor(compBusq);
                    if (porComp.isEmpty()) {
                        vista.mostrarCadena("No hay canciones registradas de ese compositor.");
                    } else {
                        porComp.forEach(vista::mostrarCadena);
                    }
                    break;

                case 11:
                    vista.mostrarCadena(modelo.reportePorFormato());
                    break;

                case 12:
                    vista.mostrarCadena("Cantidad de cantantes únicos en el catálogo: " + modelo.contarCantantesUnicos());
                    break;

                case 13:
                    vista.mostrarCadena("1. Precio | 2. Año | 3. Cantante | 4. Nombre");
                    int opcOrd = vista.leerOpcion();
                    if (opcOrd == 1) modelo.ordenarMelodiasPorPrecio();
                    else if (opcOrd == 2) modelo.ordenarMelodiasPorAnio();
                    else if (opcOrd == 3) modelo.ordenarMelodiasPorCantante();
                    else if (opcOrd == 4) modelo.ordenarMelodiasPorNombre();
                    else {
                        vista.mostrarCadena("Opción de orden no válida.");
                        break;
                    }
                    vista.mostrarCadena("Catálogo ordenado correctamente.");
                    break;

                case 14:
                    ArrayList<String> cantantes = modelo.obtenerCantantes();
                    if (cantantes.isEmpty()) {
                        vista.mostrarCadena("No hay cantantes registrados.");
                    } else {
                        vista.mostrarCadena("Cantantes con melodías:");
                        cantantes.forEach(vista::mostrarCadena);
                    }
                    break;

                case 15:
                    vista.mostrarCadena(modelo.reportePorGenero());
                    break;

                case 16:
                    String nombreEliminar = vista.leerNombreMelodia();
                    int eliminadas = modelo.removerPorNombre(nombreEliminar);
                    if (eliminadas > 0) {
                        vista.mostrarCadena("Se eliminaron " + eliminadas + " melodía(s) con ese nombre.");
                    } else {
                        vista.mostrarCadena("No se encontró ninguna melodía con ese nombre.");
                    }
                    break;

                case 17:
                    vista.mostrarCadena("Gracias por su consulta. Tenga un excelente día");
                    break;

                default:
                    vista.mostrarCadena("Opción no válida. Intenta de nuevo.");
                    break;
            }
        }
    }
}