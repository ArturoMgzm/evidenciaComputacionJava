/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.computacionJava.evidencia.com.computacionJava.evidencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utilizando el proyecto base del profesor
 * Arturo Manrique Garza
 * Al03009720
 *
 * @author jajimenez
 */
public class ConsultorioMain {

    public static List<Usuario> usuarios;
    public static List<Cita> citas = new ArrayList();
    public static List<Medico> doctores = new ArrayList();
    public static List<Paciente> pacientes = new ArrayList();

    public static void main(String[] args) throws IOException {
        boolean existeUsuario;
        String usuario = "";
        String contrasena = "";
        Scanner credenciales = new Scanner(System.in);
        System.out.println("Cargando sistema... ");
        cargarUsuarios();
        System.out.println("-------------------------Inicio de sesion-----------------------");
        System.out.println("Usuario:");
        usuario = credenciales.nextLine();
        System.out.println("Contraseña");
        contrasena = credenciales.nextLine();
        existeUsuario = validarCredenciales(usuario, contrasena);
        if (existeUsuario) {
            cargarCita();
            cargarMedicos();
            cargarPacientes();
            menu();

        } else {
            System.out.println("el usuario no existe");
            System.out.println("abandonando sistema");
        }

    }

    public static void cargarUsuarios() throws IOException{

        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }

        usuarios.add(new Usuario(1, "System", "9130"));

        String json = leerArchivoUsuarios();
        Gson gson = new Gson();
        Usuario[] usuario = gson.fromJson(json, Usuario[].class);
        //citas.add(cita);
        //System.out.println("nombre del paciente:" + cita.getPaciente().getNombre());
        if(usuario != null){
            for (Usuario temp : usuario) {
                usuarios.add(temp);
            }
        }
        System.out.println("Los usuarios han sido cargados: " + usuarios.size());

    }

    public static boolean validarCredenciales(String usuario, String contrasena) {
        return usuarios.stream().anyMatch(x -> x.getNombre().equals(usuario) && x.getContrasena().equals(contrasena));
    }

    public static void menu() {
        Integer opcion = -1;
        while (opcion != 9) {

            Scanner opcionScanner = new Scanner(System.in);
            System.out.println("1.-Dar de alta a medico\n"
                    + "2.-Dar de alta a un paciente\n"
                    + "3.Ver las citas de todos los medicos\n"
                    + "4.-Ver las citas por nombre del medico\n"
                    + "5.-Ver las citas por nombre del paciente\n"
                    + "6.-Crear cita\n"
                    + "7.-Guardar\n"
                    + "8.-Crear usuario administrador\n"
                    + "9.-Salir");
            System.out.println("Opción:");
            opcion = opcionScanner.nextInt();

            switch (opcion) {
                case 1:
                    crearDoctor();
                    break;
                case 2:
                    crearPaciente();
                    break;
                case 3:
                    imprimirTodasCitas();
                    break;
                case 4:
                    imprimirCitasDoctor();
                    break;
                case 5:
                    imprimirCitasPaciente();
                    break;
                case 6:
                    crearCita();
                    break;
                case 7:
                    save();
                    break;
                case 8:
                    crearUsuario();
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Opción no reconocida");
                    break;


            }
        }

    }

    public static void save() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(citas);
            System.out.println(json);
            String ruta = "db/citas.json";

            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (Exception e) {
            System.out.println("Error->" + e.getMessage());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(doctores);
            System.out.println(json);
            String ruta = "db/medicos.json";
            try{
                File file = new File(ruta);
                // Si el archivo no existe es creado
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(json);
                bw.close();
            }
            catch (Exception e){
                System.out.println("Error->" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error->" + e.getMessage());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(pacientes);
            System.out.println(json);
            String ruta = "db/pacientes.json";
            try{
                File file = new File(ruta);
                // Si el archivo no existe es creado
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(json);
                bw.close();
            }
            catch (Exception e){
                System.out.println("Error->" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error->" + e.getMessage());
        }
        try {
            List<Usuario> tempUsuarios = usuarios;
            tempUsuarios.remove(0);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(tempUsuarios);
            System.out.println(json);
            String ruta = "db/usuarios.json";
            try{
                File file = new File(ruta);
                // Si el archivo no existe es creado
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(json);
                bw.close();
            }
            catch (Exception e){
                System.out.println("Error->" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error->" + e.getMessage());
        }
    }

    public static void cargarCita() throws IOException {
        String json = leerArchivoCitas();
        Gson gson = new Gson();
        Cita[] cita = gson.fromJson(json, Cita[].class);
        //citas.add(cita);
        //System.out.println("nombre del paciente:" + cita.getPaciente().getNombre());
        if(cita != null){
            for (Cita temp : cita) {
                citas.add(temp);
            }
        }
    }

    public static void cargarMedicos() throws IOException {
        String json = leerArchivoMedicos();
        Gson gson = new Gson();
        Medico[] medico = gson.fromJson(json, Medico[].class);
        //citas.add(cita);
        //System.out.println("nombre del paciente:" + cita.getPaciente().getNombre());
        if(medico != null){
            for (Medico temp : medico) {
                doctores.add(temp);
            }
        }
    }

    public static void cargarPacientes() throws IOException {
        String json = leerArchivoPacientes();
        Gson gson = new Gson();
        Paciente[] paciente = gson.fromJson(json, Paciente[].class);
        //citas.add(cita);
        //System.out.println("nombre del paciente:" + cita.getPaciente().getNombre());
        if(paciente != null){
            for (Paciente temp : paciente) {
                pacientes.add(temp);
            }
        }
    }

    public static void imprimirCitasDoctor(){
        Medico doctor = new Medico();
        doctor = seleccionarMedico();
        boolean tieneCitas = false;
        if(doctor == null){
            System.out.println("Medico no reconocido");
            return;
        }
        for (Cita cita : citas) {
            if(doctor.getId() == cita.getMedico().getId()){
                tieneCitas = true;
                System.out.println("---------------------------------------------------");
                System.out.println("Nombre cita:" + cita.getNombreCita());
                System.out.println("Nombre paciente:" + cita.getPaciente().getNombre());
                System.out.println("Nombre medico:" + cita.getMedico().getNombre());
                System.out.println("Fecha:" + cita.getFecha());
                System.out.println("Hora:" + cita.getHora());
            }
        }
        if(tieneCitas)
            System.out.println("---------------------------------------------------");
    }

    public static void imprimirCitasPaciente(){
        Paciente paciente = new Paciente();
        paciente = seleccionarPaciente();
        boolean tieneCitas = false;
        if(paciente == null){
            System.out.println("Paciente no reconocido");
            return;
        }
        for (Cita cita : citas) {
            if(paciente.getId() == cita.getPaciente().getId()){
                tieneCitas = true;
                System.out.println("---------------------------------------------------");
                System.out.println("Nombre cita:" + cita.getNombreCita());
                System.out.println("Nombre paciente:" + cita.getPaciente().getNombre());
                System.out.println("Nombre medico:" + cita.getMedico().getNombre());
                System.out.println("Fecha:" + cita.getFecha());
                System.out.println("Hora:" + cita.getHora());
            }
        }
        if(tieneCitas)
            System.out.println("---------------------------------------------------");
    }

    public static void imprimirTodasCitas() {
        for (Cita cita : citas) {
            System.out.println("---------------------------------------------------");
            System.out.println("Nombre cita:" + cita.getNombreCita());
            System.out.println("Nombre paciente:" + cita.getPaciente().getNombre());
            System.out.println("Nombre medico:" + cita.getMedico().getNombre());
            System.out.println("Fecha:" + cita.getFecha());
            System.out.println("Hora:" + cita.getHora());
        }
        if(citas.size()!=0)
            System.out.println("---------------------------------------------------");
    }

    public static Medico seleccionarMedico(){
        String id;
        if(doctores.size() == 0)
            return null;
        try {
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            System.out.println("Información de todos los medicos");
            System.out.println();

            for (int i=0;i<doctores.size();i++) {
                System.out.println("Id: " + doctores.get(i).getId());
                System.out.println("Nombre: " + doctores.get(i).getNombre());
                System.out.println("Especialidad: " + doctores.get(i).getEspecialida());
                System.out.println();
            }
            System.out.println("Ingrese el id del medico que desea");
            id = bufferedReader.readLine();
            for (int i=0;i<doctores.size();i++) {
                if(Integer.parseInt(id) == doctores.get(i).getId()){
                    return doctores.get(i);
                }
            }
            System.out.println("Medico no reconocido");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Paciente seleccionarPaciente(){
        String id;
        if(pacientes.size() == 0)
            return null;
        try {
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            System.out.println("Información de todos los pacientes");
            System.out.println();

            for (int i=0;i<pacientes.size();i++) {
                System.out.println("Id: " + pacientes.get(i).getId());
                System.out.println("Nombre: " + pacientes.get(i).getNombre());
                System.out.println();
            }
            System.out.println("Ingrese el id del paciente que desea");
            id = bufferedReader.readLine();
            for (int i=0;i<pacientes.size();i++) {
                if(Integer.parseInt(id) == pacientes.get(i).getId()){
                    return pacientes.get(i);
                }
            }
            System.out.println("Paciente no reconocido");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void crearCita() {
        String fecha;
        String hora;
        String razon;
        Cita cita = new Cita();
        Medico medico = new Medico();
        Paciente paciente = new Paciente();
        try {
            System.out.println("Seleccione el medico");
            medico = seleccionarMedico();
            if(medico == null){
                System.out.println("regresando al menu principal");
                return;
            }
            System.out.println("Seleccione el paciente");
            paciente = seleccionarPaciente();
            if(paciente == null){
                System.out.println("regresando al menu principal");
                return;
            }
            System.out.println("Ingrese la fecha de la cita");
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            fecha = bufferedReader.readLine();
            System.out.println("Ingrese la hora de la cita");
            hora = bufferedReader.readLine();
            System.out.println("Ingrese la razon de la cita");
            razon = bufferedReader.readLine();
            cita.setId(citas.size()+1);
            cita.setNombreCita(razon);
            cita.setPaciente(paciente);
            cita.setMedico(medico);
            cita.setFecha(fecha);
            cita.setHora(hora);
            citas.add(cita);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void crearDoctor(){
        Medico medico = new Medico();
        String nombre;
        String especialidad;
        try {
            System.out.println("Ingrese el nombre del medico");
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            nombre = bufferedReader.readLine();
            System.out.println("Ingrese la especialidad del medico");
            especialidad = bufferedReader.readLine();
            medico.setEspecialida(especialidad);
            medico.setNombre(nombre);
            medico.setId(doctores.size()+1);
            doctores.add(medico);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void crearPaciente(){
        Paciente paciente = new Paciente();
        String nombre;
        try {
            System.out.println("Ingrese el nombre del paciente");
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            nombre = bufferedReader.readLine();
            paciente.setNombre(nombre);
            paciente.setId(pacientes.size()+1);
            pacientes.add(paciente);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void crearUsuario(){
        Usuario admin = new Usuario();
        String nombre;
        String password;
        try {
            System.out.println("Ingrese el nombre del usuario");
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            nombre = bufferedReader.readLine();
            System.out.println("Ingrese la contraseña del usuario");
            password = bufferedReader.readLine();
            admin.setContrasena(password);
            admin.setNombre(nombre);
            admin.setId(usuarios.size()+1);
            usuarios.add(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String leerArchivoCitas() throws IOException {
        String archivo = "db/citas.json";
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            StringBuilder json = new StringBuilder();
            String cadena;
            while ((cadena = b.readLine()) != null) {
                json.append(cadena);
            }
            b.close();
            return json.toString();
        }catch(Exception e){
            System.out.println("Error->" + e.getMessage());
            return null;
        }
    }

    public static String leerArchivoMedicos() throws IOException {
        String archivo = "db/medicos.json";
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            StringBuilder json = new StringBuilder();
            String cadena;
            while ((cadena = b.readLine()) != null) {
                json.append(cadena);
            }
            b.close();
            return json.toString();
        }catch(Exception e){
            System.out.println("Error->" + e.getMessage());
            return null;
        }
    }

    public static String leerArchivoPacientes() throws IOException {
        String archivo = "db/pacientes.json";
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            StringBuilder json = new StringBuilder();
            String cadena;
            while ((cadena = b.readLine()) != null) {
                json.append(cadena);
            }
            b.close();
            return json.toString();
        }catch(Exception e){
            System.out.println("Error->" + e.getMessage());
            return null;
        }
    }

    public static String leerArchivoUsuarios() throws IOException {
        String archivo = "db/usuarios.json";
        try{
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            StringBuilder json = new StringBuilder();
            String cadena;
            while ((cadena = b.readLine()) != null) {
                json.append(cadena);
            }
            b.close();
            return json.toString();
        }catch(Exception e){
            System.out.println("Error->" + e.getMessage());
            return null;
        }
    }

}
