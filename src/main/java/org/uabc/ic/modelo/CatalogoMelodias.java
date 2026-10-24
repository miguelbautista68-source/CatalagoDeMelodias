package org.uabc.ic.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CatalogoMelodias {
    private ArrayList<Melodia> canciones = new ArrayList<>();

    // Regresa true si YA existía  y false si se agregó
    public boolean registrarMelodia(Melodia obj) {
        boolean bandera = false;

        for (Melodia melodia : canciones) {
            bandera = melodia.equals(obj);
            if (bandera)
                break;
        }
        if (!bandera)
            canciones.add(obj);

        return bandera;
    }

    // Busca por nombre
    public ArrayList<Melodia> buscar(String nombre) {
        return canciones.stream()
                .filter(m -> m.getNombre() != null
                        && m.getNombre().toLowerCase().contains(nombre.trim().toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Busca por ID
    public Melodia buscar(int id) {
        for (Melodia m : canciones) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public boolean removerMelodia(int id) {
        return canciones.removeIf(m -> m.getId() == id);
    }

    // Elimina todas las versiones con ese nombre y regresa cuántas eliminó
    public int removerPorNombre(String nombre) {
        int antes = canciones.size();
        canciones.removeIf(m -> m.getNombre() != null
                && m.getNombre().equalsIgnoreCase(nombre.trim()));
        return antes - canciones.size();
    }

    public boolean actualizarMelodia(int id, String nombre, String compositor, int anio,
                                     String genero, String cantante, double precio, String formato) {
        Melodia objetivo = buscar(id);
        if (objetivo == null) return false;

        // Evitar que quede duplicada con otra (mismo cantante y año)
        Melodia temporal = new Melodia(nombre, compositor, anio, genero, cantante, precio, formato);
        for (Melodia m : canciones) {
            if (m != objetivo && m.equals(temporal)) return false;
        }

        objetivo.setNombre(nombre);
        objetivo.setCompositor(compositor);
        objetivo.setAnio(anio);
        objetivo.setGenero(genero);
        objetivo.setCantante(cantante);
        objetivo.setPrecio(precio);
        objetivo.setFormato(formato);
        return true;
    }

    public double obtenerPrecioColeccion() {
        return canciones.stream().mapToDouble(Melodia::getPrecio).sum();
    }

    public ArrayList<String> obtenerTodosLosGeneros() {
        return canciones.stream()
                .map(Melodia::getGenero)
                .filter(Objects::nonNull)
                .map(g -> g.trim().toLowerCase())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public HashSet<String> obtenerTodasLosGererosIteractivo() {
        HashSet<String> generos = new HashSet<>();
        for (Melodia cancion : canciones) {
            if (cancion.getGenero() != null) {
                generos.add(cancion.getGenero().trim().toLowerCase());
            }
        }
        return generos;
    }
    // Lista de cantantes que tienen melodías (sin repetir, ordenados)
    public ArrayList<String> obtenerCantantes() {
        return canciones.stream()
                .map(Melodia::getCantante)
                .filter(Objects::nonNull)
                .map(String::trim)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Melodías agrupadas por género
    public String reportePorGenero() {
        if (canciones.isEmpty()) {
            return "No hay canciones registradas en el catálogo.";
        }
        Map<String, List<String>> porGenero = canciones.stream()
                .filter(m -> m.getGenero() != null)
                .collect(Collectors.groupingBy(
                        m -> m.getGenero().trim().toLowerCase(),
                        TreeMap::new,
                        Collectors.mapping(Melodia::getNombre, Collectors.toList())));

        StringBuilder sb = new StringBuilder(" Melodías por Género \n");
        porGenero.forEach((genero, nombres) ->
                sb.append(genero).append(": ").append(nombres).append("\n"));
        return sb.toString();
    }

    public ArrayList<Melodia> melodiasPorAnio(int anio) {
        return canciones.stream()
                .filter(m -> m.getAnio() == anio)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Melodia> melodiasPorCantante(String cantante) {
        return canciones.stream()
                .filter(m -> m.getCantante() != null && m.getCantante().equalsIgnoreCase(cantante.trim()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Regresa los nombres de las melodías de ese compositor
    public ArrayList<String> melodiasPorCompositor(String compositor) {
        return canciones.stream()
                .filter(m -> m.getCompositor() != null && m.getCompositor().equalsIgnoreCase(compositor.trim()))
                .map(Melodia::getNombre)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void ordenarMelodiasPorPrecio() {
        canciones.sort(Comparator.comparingDouble(Melodia::getPrecio));
    }

    public void ordenarMelodiasPorAnio() {
        canciones.sort(Comparator.comparingInt(Melodia::getAnio));
    }

    public void ordenarMelodiasPorCantante() {
        canciones.sort(Comparator.comparing(Melodia::getCantante, String.CASE_INSENSITIVE_ORDER));
    }

    public void ordenarMelodiasPorNombre() {
        canciones.sort(Comparator.comparing(Melodia::getNombre, String.CASE_INSENSITIVE_ORDER));
    }

    public String mostrarTodasMelodias() {
        if (canciones.isEmpty()) {
            return "El catálogo está vacío";
        }
        StringBuilder sb = new StringBuilder();
        for (Melodia melodia : canciones) {
            sb.append(melodia).append("\n");
        }
        return sb.toString();
    }

    // Cuántos cantantes únicos existen en el catálogo
    public int contarCantantesUnicos() {
        return (int) canciones.stream()
                .map(Melodia::getCantante)
                .filter(c -> c != null)
                .map(String::trim)
                .map(String::toUpperCase)
                .distinct()
                .count();
    }

    public String reportePorFormato() {
        if (canciones.isEmpty()) {
            return "No hay canciones registradas en el catálogo.";
        }

        Map<String, Long> conteoFormatos = canciones.stream()
                .filter(m -> m.getFormato() != null)
                .collect(Collectors.groupingBy(m -> m.getFormato().toLowerCase(), Collectors.counting()));

        StringBuilder sb = new StringBuilder(" Canciones por Formato \n");
        conteoFormatos.forEach((formato, cantidad) ->
                sb.append("Formato: ").append(formato).append(" -> ").append(cantidad).append(" melodía(s)\n")
        );
        return sb.toString();
    }
}