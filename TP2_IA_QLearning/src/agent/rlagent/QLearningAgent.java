package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
    //TODO

    protected HashMap<Etat, Map<Action, Double>> Q = new HashMap<>();

    /**
     *
     * @param alpha
     * @param gamma
     * @param Environnement
     */
    public QLearningAgent(double alpha, double gamma,
            Environnement _env) {
        super(alpha, gamma, _env);
        //TODO

    }

    /**
     * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
     *
     * renvoi liste vide si aucunes actions possibles dans l'etat
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        //TODO
        List<Action> r = new ArrayList<>();

        double max_value = -1;

        for (Action a : env.getActionsPossibles(e)) {

            if (this.env.estAbsorbant()) {
                return null;
            }

            double value = 0.0;
            try {
                value = getValeur(e);
            } catch (Exception ex) {
            }
            if (value >= max_value) {
                // Meilleure valeur
                if (value > max_value) {
                    r.clear();
                }
                r.add(a);
                max_value = value;
            }
        }

        return r;
    }

    /**
     * @return la valeur d'un etat return max Q( s,a)
     */
    @Override
    public double getValeur(Etat e) {
        //TODO
        double max_value = -1.0;
        for (Action a : env.getActionsPossibles(e)) {
            if (max_value == -1 || getQValeur(e, a) > max_value) {
                max_value = getQValeur(e, a);
            }
        }
        return max_value;

    }

    /**
     *
     * @param e
     * @param a
     * @return Q valeur du couple (e,a)
     */
    @Override
    public double getQValeur(Etat e, Action a) {
        Map<Action, Double> m = Q.get(e);
        if (m == null) {
            return 0.0;
        }
        return (m.get(a) == null) ? 0 : m.get(a);
        
    }

    /**
     * setter sur Q-valeur
     */
    @Override
    public void setQValeur(Etat e, Action a, double d) {
        Map<Action, Double> m = Q.get(e);
        if (m == null) {
            m = new HashMap<>();
            m.put(a, d);
            Q.put(e, m);
        } else {
            m.replace(a, d);
        }
        
        //TODO
        if (d > vmax) {
            vmax = d;
        }
        if (d < vmin) {
            vmin = d;
        }
        //mise a jour vmin et vmax pour affichage gradient de couleur
        //...
        this.notifyObs();
    }

    /**
     *
     * mise a jour de la Q-valeur du couple (e,a) apres chaque interaction
     * <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement
     * apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        //TODO
        setQValeur(e, a, (1 - alpha) * getQValeur(e, a) + alpha * (reward + gamma * (getValeur(esuivant))));
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    /**
     * reinitialise les Q valeurs
     */
    @Override
    public void reset() {
        super.reset();
        this.episodeNb = 0;
        //TODO
        this.Q.clear();

        this.notifyObs();
    }

}
