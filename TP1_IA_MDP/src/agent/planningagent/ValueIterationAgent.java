package agent.planningagent;

import java.util.List;
import java.util.Random;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration et choisit
 * ses actions selon la politique calculee.
 *
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent {

    /**
     * discount facteur
     */
    protected double gamma;
    //*** VOTRE CODE
    protected HashMap<Etat, Double> map;

    /**
     *
     * @param gamma
     * @param mdp
     * @param map
     */
    public ValueIterationAgent(double gamma, MDP mdp) {
        super(mdp);
        this.gamma = gamma;
        //*** VOTRE CODE

    }

    public ValueIterationAgent(MDP mdp) {
        this(0.9, mdp);

    }

    /**
     *
     * Mise a jour de V: effectue UNE iteration de value iteration
     */
    @Override
    public void updateV() {
        //delta est utilise pour detecter la convergence de l'algorithme
        //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
        //delta < epsilon 
        this.delta = 0.0;
        //*** VOTRE CODE
        HashMap<Etat, Double> previousV = (HashMap<Etat, Double>) this.map.clone();

        for (Etat e : this.mdp.getEtatsAccessibles()) {
            List<Action> l = mdp.getActionsPossibles(e);
            Double max_value = null;
            for (Action a : l) {
                try {
                    Map<Etat, Double> possibilites = mdp.getEtatTransitionProba(e, a);
                    Double value = 0.0;
                    for (Map.Entry<Etat, Double> entry : possibilites.entrySet()) {
                        if (possibilites == null || entry.getValue().compareTo(possibilites.getValue()) > 0) {
                            possibilites = entry;
                        }
                    }
                } catch (Exception ex) {
                }
            }
        }
        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        //******************* a laisser a la fin de la methode
        this.notifyObs();
    }

    /**
     * renvoi l'action executee par l'agent dans l'etat e Si aucune actions
     * possibles, renvoi NONE ou null.
     */
    @Override
    public Action getAction(Etat e) {
        //*** VOTRE CODE
        List<Action> liste = this.getPolitique(e);
        if (liste.size() > 0) {
            return liste.get(new Random().nextInt(liste.size()));
        }
        return null;

    }

    @Override
    public double getValeur(Etat _e) {
        //*** VOTRE CODE
        return this.map.get(_e);
    }

    /**
     * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si
     * aucune action n'est possible)
     */
    @Override
    public List<Action> getPolitique(Etat _e) {
        List<Action> l = mdp.getActionsPossibles(_e);
        List<Action> r = new ArrayList<>();
        Double max_value = null;

        for (Action a : l) {

            try {
                Map<Etat, Double> possibilites = mdp.getEtatTransitionProba(_e, a);
                Double value = 0.0;
                for (Map.Entry<Etat, Double> entry : possibilites.entrySet()) {
                    value += entry.getValue();
                }
                if (max_value == null || value > max_value) {
                    r.clear();
                    r.add(a);
                    max_value = value;
                } else if (Objects.equals(max_value, value)) {
                    r.add(a);
                }
            } catch (Exception ex) {
            }

        }

        return r;

    }

    @Override
    public void reset() {
        super.reset();
        //*** VOTRE CODE

        /*-----------------*/
        this.notifyObs();

    }

    @Override
    public void setGamma(double arg0) {
        this.gamma = arg0;
    }

}
