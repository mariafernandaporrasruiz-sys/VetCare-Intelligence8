import javax.swing.*;
import java.awt.*;

public class PetCareWindow extends JFrame {
    private JTextField ownerNameField;
    private JTextField petNameField;
    private JTextField speciesField;
    private JTextField breedField;
    private JTextField ageField;
    private JTextField vaccineField;
    private JTextField nextVaccineField;
    private JTextField diagnosisField;
    private JTextArea treatmentArea;
    private JTextArea resultArea;

    public PetCareWindow() {
        setTitle("Smart PetCare Assistant");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 1, 8, 8));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Smart PetCare Assistant", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        ownerNameField = new JTextField("Maria Fernanda Porras Ruiz");
        petNameField = new JTextField("Luna");
        speciesField = new JTextField("Dog");
        breedField = new JTextField("Golden Retriever");
        ageField = new JTextField("3");
        vaccineField = new JTextField("Rabies Vaccine");
        nextVaccineField = new JTextField("2027-05-20");
        diagnosisField = new JTextField("Healthy condition");
        treatmentArea = new JTextArea("Continue regular vaccination and check-ups");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JButton generateButton = new JButton("Generate AI Recommendation");

        mainPanel.add(title);
        mainPanel.add(new JLabel("Owner Name:"));
        mainPanel.add(ownerNameField);

        mainPanel.add(new JLabel("Pet Name:"));
        mainPanel.add(petNameField);

        mainPanel.add(new JLabel("Species:"));
        mainPanel.add(speciesField);

        mainPanel.add(new JLabel("Breed:"));
        mainPanel.add(breedField);

        mainPanel.add(new JLabel("Age:"));
        mainPanel.add(ageField);

        mainPanel.add(new JLabel("Vaccine Name:"));
        mainPanel.add(vaccineField);

        mainPanel.add(new JLabel("Next Vaccine Date:"));
        mainPanel.add(nextVaccineField);

        mainPanel.add(new JLabel("Medical Diagnosis:"));
        mainPanel.add(diagnosisField);

        mainPanel.add(new JLabel("Treatment:"));
        mainPanel.add(new JScrollPane(treatmentArea));

        mainPanel.add(generateButton);

        mainPanel.add(new JLabel("AI Recommendation:"));
        mainPanel.add(new JScrollPane(resultArea));

        add(mainPanel);

        generateButton.addActionListener(e -> generateRecommendation());
    }

    private void generateRecommendation() {
        try {
            String ownerName = ownerNameField.getText();
            String petName = petNameField.getText();
            String species = speciesField.getText();
            String breed = breedField.getText();
            int age = Integer.parseInt(ageField.getText());
            String vaccineName = vaccineField.getText();
            String nextVaccineDate = nextVaccineField.getText();
            String diagnosis = diagnosisField.getText();
            String treatment = treatmentArea.getText();

            Owner owner = new Owner(1, ownerName, "N/A", "N/A");
            Pet pet = new Pet(1, petName, species, breed, age, owner.getId());

            Vaccine vaccine = new Vaccine(
                    1,
                    pet.getId(),
                    vaccineName,
                    "2026-05-20",
                    nextVaccineDate
            );

            MedicalRecord medicalRecord = new MedicalRecord(
                    1,
                    pet.getId(),
                    "Information entered in the local system.",
                    diagnosis,
                    treatment,
                    "2026-05-25"
            );

            AIService aiService = new AIService();
            String recommendation = aiService.generatePetCareAdvice(pet, vaccine, medicalRecord);

            resultArea.setText(recommendation);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
        }
    }
}