    package com.example.pigeon.service.impl;

    import com.example.pigeon.dto.UtilisateurDto;
    import com.example.pigeon.entity.Role;
    import com.example.pigeon.entity.Utilisateur;
    import com.example.pigeon.exception.ResourceNotFoundException;
    import com.example.pigeon.repository.UtilisateurRepository;
    import com.example.pigeon.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class UserServiceImpl implements UserService {

        @Autowired
        private UtilisateurRepository utilisateurRepository;

        public Utilisateur findByUsernameAndMotDePasse(String username, String motDePasse) {
            return utilisateurRepository.findByUsernameAndMotDePasse(username, motDePasse);
        }

        @Override
        public UtilisateurDto registerUtilisateur(UtilisateurDto userDto) {
            if (utilisateurRepository.findByUsername(userDto.getUsername()).isPresent()) {
                throw new ResourceNotFoundException("Un éleveur avec ce nom de colombier existe déjà");
            }
            if (userDto.getRole() == null) {
                userDto.setRole(Role.ROLE_USER);
            }
            Utilisateur utilisateur = userDto.toEntity();
//            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
            return UtilisateurDto.toDto(savedUtilisateur);
        }


        @Override
        public List<Utilisateur> getAllUtilisateurs() {
            return utilisateurRepository.findAll();
        }

        @Override
        public UtilisateurDto changeUserRole(Long userId, Role newRole) {
            Utilisateur utilisateur = utilisateurRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

            utilisateur.setRole(newRole);
            utilisateurRepository.save(utilisateur);

            return UtilisateurDto.toDto(utilisateur);
        }
    }
