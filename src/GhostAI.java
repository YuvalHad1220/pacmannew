public interface GhostAI {

    void Chase(Pacman p);
    void Scatter(Pacman p);
    void Frightened(Pacman p);
    void Eaten(Pacman p);
    void Respawning(Pacman p);
    void LeavingGhostPen(Pacman p);
    void Dead(Pacman p);
    void Paused(Pacman p);

}