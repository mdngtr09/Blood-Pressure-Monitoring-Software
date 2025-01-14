/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package atv4;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author oneide
 */
public class Atv41 {



    public static void main(String[] args) {
        // J principal
        JFrame frame = new JFrame("Monitor de Pressão - Sindicato de Nutricionistas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // P principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Componentes
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel lblData = new JLabel("Data (dd/MM/yyyy):");
        JFormattedTextField txtData = createFormattedField("##/##/####");
        txtData.setToolTipText("Informe a data no formato dd/MM/yyyy");

        JLabel lblHora = new JLabel("Hora (HH:mm):");
        JFormattedTextField txtHora = createFormattedField("##:##");
        txtHora.setToolTipText("Informe a hora no formato HH:mm");

        JLabel lblSistolica = new JLabel("Pressão Sistólica:");
        JTextField txtSistolica = new JTextField(15);
        txtSistolica.setToolTipText("Informe o valor da pressão sistólica (inteiro)");

        JLabel lblDiastolica = new JLabel("Pressão Diastólica:");
        JTextField txtDiastolica = new JTextField(15);
        txtDiastolica.setToolTipText("Informe o valor da pressão diastólica (inteiro)");

        JCheckBox chkEstresse = new JCheckBox("Situação de estresse", false);
        chkEstresse.setToolTipText("Marque se você está em uma situação de estresse");

        inputPanel.add(lblData);
        inputPanel.add(txtData);
        inputPanel.add(lblHora);
        inputPanel.add(txtHora);
        inputPanel.add(lblSistolica);
        inputPanel.add(txtSistolica);
        inputPanel.add(lblDiastolica);
        inputPanel.add(txtDiastolica);
        inputPanel.add(new JLabel()); // vazio
        inputPanel.add(chkEstresse);

        JButton btnSalvar = new JButton("Salvar");

        JTextArea txtHistorico = new JTextArea(10, 40);
        txtHistorico.setEditable(false);
        txtHistorico.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollHistorico = new JScrollPane(txtHistorico);

        // Carrega histórico quando iniciar
        ArrayList<String> historico = new ArrayList<>();
        File arquivo = new File("dados_pressao.txt");
        if (arquivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    historico.add(linha);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao carregar histórico!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        historico.forEach(txtHistorico::append);

        // botão salvar
        btnSalvar.addActionListener((ActionEvent e) -> {
            try {
                String data = txtData.getText().trim();
                String hora = txtHora.getText().trim();
                int sistolica = Integer.parseInt(txtSistolica.getText().trim());
                int diastolica = Integer.parseInt(txtDiastolica.getText().trim());
                boolean estresse = chkEstresse.isSelected();
                
                String registro = String.format("%s %s | Sistólica: %d | Diastólica: %d | Estresse: %s\n",
                        data, hora, sistolica, diastolica, estresse ? "Sim" : "Não");
                
                historico.add(registro);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                    writer.write(registro);
                }
                
                txtHistorico.append(registro);
                
                JOptionPane.showMessageDialog(frame, "Dados salvos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                txtData.setValue(null);
                txtHora.setValue(null);
                txtSistolica.setText("");
                txtDiastolica.setText("");
                chkEstresse.setSelected(false);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, informe valores válidos para as pressões.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Adiciona componentes ao p principal
        panel.add(inputPanel);
        panel.add(btnSalvar);
        panel.add(scrollHistorico);

        frame.add(panel);

        // Exibe a j
        frame.setVisible(true);
    }

    private static JFormattedTextField createFormattedField(String format) {
        try {
            MaskFormatter formatter = new MaskFormatter(format);
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar campo formatado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return new JFormattedTextField();
        }
    }
    
}
