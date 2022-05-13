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
            System.out.println("existe el usuario");
            cargarCita();
            cargarMedicos();
            menu();

        } else {
            System.out.println("el usuario no existe");
        }

    }

    public static void cargarUsuarios() {

        if (usuarios == null) {
            usuarios = new ArrayList<>();
        }

        usuarios.add(new Usuario(1, "carlos", "1234"));
        usuarios.add(new Usuario(2, "sofia", "1234"));
        usuarios.add(new Usuario(2, "ithan", "0000"));
        usuarios.add(new Usuario(2, "alfredo", "0000"));
        System.out.println("Los usuarios han sido cargados: " + usuarios.size());

    }

    public static boolean validarCredenciales(String usuario, String contrasena) {
        return usuarios.stream().anyMatch(x -> x.getNombre().equals(usuario) && x.getContrasena().equals(contrasena));
    }

    public static void menu() {
        Integer opcion = -1;
        while (opcion != 0) {

            Scanner opcionScanner = new Scanner(System.in);
            System.out.println("1.-Dar de alta a medico\n"
                    + "2.-Dar de alta a un paciente\n"
                    + "4.Ver las citas de todos los medicos\n"
                    + "5.-Ver las citas por nombre del medico\n"
                    + "6.-Ver las citas por nombre del paciente\n"
                    + "7.-Crear cita\n"
                    + "8.-ver todas las citas\n"
                    + "9.-Guardar\n"
                    + "0.-Salir");
            System.out.println("Opción:");
            opcion = opcionScanner.nextInt();

            switch (opcion) {
                case 1:
                    crearDoctor();
                    break;
                case 2:
                    crearPaciente();
                    break;
                case 7:
                    crearCita();
                    break;
                case 8:
                    imprimirTodasCitas();
                    break;
                case 9:
                    save();
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

    public static void imprimirTodasCitas() {
        for (Cita cita : citas) {
            System.out.println("---------------------------------------------------");
            System.out.println("Nombre cita:" + cita.getNombreCita());
            System.out.println("Nombre paciente:" + cita.getPaciente().getNombre());
            System.out.println("Nombre medico:" + cita.getMedico().getNombre());
        }
    }

    public static void crearCita() {

        Cita cita = new Cita();
        Medico medico = new Medico();
        Paciente paciente = new Paciente();
        medico.setId(1);
        medico.setNombre("Salvador");
        medico.setEspecialida("General");
        paciente.setId(1);
        paciente.setNombre("Maria");
        cita.setId(1);
        cita.setNombreCita("Cita numero 1");
        cita.setMedico(medico);
        cita.setPaciente(paciente);
        cita.setFecha("24/11/2021");
        //save(cita);
        citas.add(cita);
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

}
